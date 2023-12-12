package com.ruoyi.common.utils;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AnswerExtractor {

    public static String extractAnswersToDocx(String input, String url) throws Exception {
        // 使用正则表达式匹配 "answer" 字段
        Pattern pattern = Pattern.compile("\"answer\": \"(.*?)\"", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(input);

        // 创建一个新的 Word 文档
        XWPFDocument document = new XWPFDocument();

        // 为每个匹配的 "answer" 创建一个新段落
        while (matcher.find()) {
            String answer = matcher.group(1).replace("\\n", "\n"); // 将转义的换行符替换为实际的换行符
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.createRun().setText(answer);
        }
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath();
        String fileName = FileUploadUtils.getFileName("法律意见书.docx");
        String fileUploadAbsolutePath = FileUploadUtils.getAbsPath(filePath, fileName);
        // 将文档写入到指定的文件路径
        try (FileOutputStream out = new FileOutputStream(fileUploadAbsolutePath)) {
            document.write(out);
            return url + FileUploadUtils.getPathFileName(filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
