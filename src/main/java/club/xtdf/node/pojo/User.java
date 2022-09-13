package club.xtdf.node.tree.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String id;
    private String pid;
    private List<User> children = new ArrayList<>();

    public User() {
        this.id = "0";
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, String pid) {
        this.id = id;
        this.pid = pid;
    }

    public User(String id, String pid, List<User> children) {
        this.id = id;
        this.pid = pid;
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<User> getChildren() {
        return children;
    }

    public void setChildren(List<User> children) {
        this.children = children;
    }
}
