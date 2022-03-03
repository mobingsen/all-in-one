package org.tea.plus.agent.collector.collection;

import org.tea.plus.agent.collector.init.NotProguard;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 错误信息统计
 *
 * @author mobingsen
 */
@Data
@EqualsAndHashCode
@ToString
@NotProguard
public class ErrorLog {
    private String logType;
    private String statck;
    private String errorMsg;
    private String errorType;
    private String ip;
    private String keyId;
    private Long createTime;
}
