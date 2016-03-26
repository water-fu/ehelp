package com.wisdom.web.view;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.List;

/**
 * 树实体
 * Created by fusj on 16/3/14.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Tree implements Serializable {
    // 显示内容
    private String text;

    // 编码
    private String id;

    // 层级
    private String level;

    // 编码
    private String code;

    // 简拼
    private String simplePinyin;

    // 子节点
    private List<Tree> nodes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSimplePinyin() {
        return simplePinyin;
    }

    public void setSimplePinyin(String simplePinyin) {
        this.simplePinyin = simplePinyin;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Tree> getNodes() {
        return nodes;
    }

    public void setNodes(List<Tree> nodes) {
        this.nodes = nodes;
    }
}
