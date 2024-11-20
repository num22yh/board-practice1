package com.study.model;

public class Attachment {
    private int id;
    private int postId;
    private String originalName;
    private String storedName;
    private String logicalPath;
    private String physicalPath;
    private long size;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getPostId(){
        return postId;
    }

    public void setPostId(int postId){
        this.postId = postId;
    }

    public String getOriginalName(){
        return originalName;
    }

    public void setOriginalName(String originalName){
        this.originalName = originalName;
    }

    public String getStoredName(){
        return storedName;
    }

    public void setStoredName(String storedName){
        this.storedName = storedName;
    }

    public String getLogicalPath(){
        return logicalPath;
    }

    public void setLogicalPath(String logicalPath){
        this.logicalPath = logicalPath;

    }

    public String getPhysicalPath(){
        return physicalPath;
    }

    public void setPhysicalPath(String physicalPath){
        this.physicalPath = physicalPath;
    }

    public long getSize(){
        return size;
    }

    public void setSize(long size){
        this.size = size;
    }
}
