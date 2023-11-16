INSERT INTO `ry-vue`.`sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,
                                      `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`,
                                      `update_time`, `remark`)
VALUES (100, 1, '合同草拟', '1', 'sys_contract_status', '', 'info', 'N', '0', 'admin', '2023-11-16 08:51:05', '', NULL,
        '合同草拟');
INSERT INTO `ry-vue`.`sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,
                                      `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`,
                                      `update_time`, `remark`)
VALUES (101, 2, '内部审核', '2', 'sys_contract_status', '', 'info', 'N', '0', 'admin', '2023-11-16 08:51:05', '', NULL,
        '合同草拟');
INSERT INTO `ry-vue`.`sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,
                                      `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`,
                                      `update_time`, `remark`)
VALUES (102, 3, '法律审查', '3', 'sys_contract_status', '', 'info', 'N', '0', 'admin', '2023-11-16 08:51:05', '', NULL,
        '合同草拟');
INSERT INTO `ry-vue`.`sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,
                                      `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`,
                                      `update_time`, `remark`)
VALUES (103, 4, '合同比对', '4', 'sys_contract_status', '', 'info', 'N', '0', 'admin', '2023-11-16 08:51:05', '', NULL,
        '合同草拟');
INSERT INTO `ry-vue`.`sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`,
                                      `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`,
                                      `update_time`, `remark`)
VALUES (104, 5, '归档', '5', 'sys_contract_status', '', 'info', 'N', '0', 'admin', '2023-11-16 08:51:05', '', NULL,
        '合同草拟');


CREATE TABLE IF NOT EXISTS `sys_contract_info`
(
    `id`                        int(11)       NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `uuid`                      varchar(36)   NOT NULL COMMENT '合同uuid',
    `contract_name`             varchar(36)   NOT NULL COMMENT '合同名称',
    `contract_type`             int(11)       DEFAULT NULL COMMENT '合同类型',
    `contract_template`         varchar(256)  DEFAULT NULL COMMENT '使用范本',
    `contract_template_name`    varchar(256)  DEFAULT NULL COMMENT '使用范本文件名',
    `need_law_supervise`        int(11)       DEFAULT NULL COMMENT '是否需要法审，0-不需要，1-需要',
    `not_supervise_reason`      int(11)       DEFAULT NULL COMMENT '无需法审原因',
    `contract_status`           int(11)       NOT NULL COMMENT '合同审批状态',
    `contract_file`             varchar(256)  DEFAULT NULL COMMENT '合同存储路径',
    `contract_file_name`        varchar(256)  DEFAULT NULL COMMENT '合同文件名',
    `owner`                     varchar(64)   NOT NULL COMMENT '合同发起人',
    `verify_result`             varchar(256)  DEFAULT NULL COMMENT '比对意见',
    `create_time`               DATETIME      NOT NULL COMMENT '合同创建时间',
    `update_time`               DATETIME      NOT NULL COMMENT '合同更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uuid` (`uuid`) USING BTREE
) ENGINE = InnoDB COMMENT ='合同信息表';


CREATE TABLE IF NOT EXISTS `sys_execution_info`
(
    `id`                    int(11)       NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `uuid`                  varchar(36)   NOT NULL COMMENT '合同uuid',
    `contract_name`         varchar(36)   NOT NULL COMMENT '合同名称',
    `contract_status`       int(11)       NOT NULL COMMENT '合同审批状态',
    `execution_user`        varchar(64)   NOT NULL COMMENT '合同处理人',
    `execution_operation`   int(11)       NOT NULL COMMENT '合同处理操作, 0-提交，1-通过，2-不通过',
    `execution_comment`     varchar(2048) NOT NULL COMMENT '处理意见',
    `execute_time`          DATETIME      NOT NULL COMMENT '操作执行时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uuid` (`uuid`) USING BTREE
    ) ENGINE = InnoDB COMMENT ='合同处理流程表';