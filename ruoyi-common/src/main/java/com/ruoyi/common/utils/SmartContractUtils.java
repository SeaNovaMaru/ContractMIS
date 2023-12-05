package com.ruoyi.common.utils;

import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ruoyi.common.config.SmartContractProperties;
import com.ruoyi.common.core.domain.entity.LoanInfo;
import io.grpc.ChannelCredentials;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.GatewayException;
import org.hyperledger.fabric.client.Network;
import org.hyperledger.fabric.client.SubmitException;
import org.hyperledger.fabric.client.identity.Identities;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.client.identity.Signer;
import org.hyperledger.fabric.client.identity.Signers;
import org.hyperledger.fabric.client.identity.X509Identity;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@Component
@AllArgsConstructor
public class SmartContractUtils {

    private final SmartContractProperties smartContractProperties;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Contract initializeContract(final Gateway gateway) {
        Network network = gateway.getNetwork(smartContractProperties.getChannelName());
        // 访问流程日志，请将 "LoanInfoContract" 修改为 "WorkflowLogContract"
        return network.getContract(smartContractProperties.getChaincodeName(), "LoanInfoContract");
    }

    public void operateContract(LoanInfo loanInfo, String operation) throws Exception {
        ManagedChannel channel = newGrpcConnection();
        Gateway.Builder builder = Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));
        try (Gateway gateway = builder.connect()) {
            Contract contract = initializeContract(gateway);
            switch (operation) {
                case "create":
                    createLoanInfo(loanInfo, contract);
                    break;
                case "update":
                    updateLoanInfo(loanInfo, contract);
                    break;
                case "delete":
                    deleteLoanInfo(loanInfo, contract);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public LoanInfo getLoanInfo(String id) throws Exception {
        ManagedChannel channel = newGrpcConnection();
        Gateway.Builder builder = Gateway.newInstance().identity(newIdentity()).signer(newSigner()).connection(channel)
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES));
        try (Gateway gateway = builder.connect()) {
            Contract contract = initializeContract(gateway);
            return readLoanInfo(id, contract);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
        return null;
    }


    private ManagedChannel newGrpcConnection() throws IOException {
        ChannelCredentials credentials = TlsChannelCredentials.newBuilder()
                .trustManager(Paths.get(smartContractProperties.getTlsCertPath()).toFile())
                .build();
        return Grpc.newChannelBuilder(String.format("%s:%s", smartContractProperties.getPeerEndPointIp(),
                        smartContractProperties.getPeerEndPointPort()), credentials)
                .overrideAuthority(smartContractProperties.getOverrideAuth())
                .build();
    }

    private Identity newIdentity() throws IOException, CertificateException {
        BufferedReader certReader = Files.newBufferedReader(Paths.get(smartContractProperties.getCertPath()));
        X509Certificate certificate = Identities.readX509Certificate(certReader);

        return new X509Identity(smartContractProperties.getMspId(), certificate);
    }

    private Signer newSigner() throws IOException, InvalidKeyException {
        BufferedReader keyReader = Files.newBufferedReader(getPrivateKeyPath());
        PrivateKey privateKey = Identities.readPrivateKey(keyReader);

        return Signers.newPrivateKeySigner(privateKey);
    }

    private Path getPrivateKeyPath() throws IOException {
        try (Stream<Path> keyFiles = Files.list(Paths.get(smartContractProperties.getKeyDirPath()))) {
            return keyFiles.findFirst().orElse(null);
        }
    }

    private void createLoanInfo(LoanInfo loanInfo, Contract contract) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        log.info("\n--> Submit Transaction: createLoanInfo, info is {}", JSON.toJSONString(loanInfo));
        contract.submitTransaction("createLoanInfo", loanInfo.toJSONString());
        log.info("Transaction committed successfully");
    }

    private LoanInfo readLoanInfo(String contractId, Contract contract) throws GatewayException {
        log.info("Submit Transaction: readLoanInfo, id is {}", contractId);
        byte[] result = contract.evaluateTransaction("readLoanInfo", contractId);
        String resultStr = prettyJson(result);
        log.info("*** Result: " + resultStr);
        return JSON.parseObject(resultStr, LoanInfo.class);
    }

    private void updateLoanInfo(LoanInfo loanInfo, Contract contract) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        log.info("\n--> Submit Transaction: updateLoanInfo, info is {}", JSON.toJSONString(loanInfo));
        contract.submitTransaction("updateLoanInfo", loanInfo.toJSONString());
        log.info("*** Transaction committed successfully");
    }

    private void deleteLoanInfo(LoanInfo loanInfo, Contract contract) throws EndorseException, SubmitException, CommitStatusException, CommitException {
        log.info("Submit Transaction: deleteLoanInfo, id is {}", loanInfo.getContractId());
        contract.submitTransaction("deleteLoanInfo", loanInfo.getContractId());
        log.info("*** Transaction committed successfully");
    }

    private String prettyJson(final byte[] json) {
        return prettyJson(new String(json, StandardCharsets.UTF_8));
    }

    private String prettyJson(final String json) {
        JsonElement parsedJson = JsonParser.parseString(json);
        return gson.toJson(parsedJson);
    }
}
