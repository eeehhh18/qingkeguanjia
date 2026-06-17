package com.qk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 线索跟进记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("clue_track_record")
public class ClueTrackRecord {

    /**
     * 记录id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 线索id
     */
    private Integer clueId;

    /**
     * 跟进人id
     */
    private Integer userId;

    /**
     * 跟进内容
     */
    private String content;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
