package club.xtdf.node;

import club.xtdf.node.pojo.User;
import club.xtdf.node.utils.ForestNodeMerger;

import java.util.LinkedList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        long x = System.currentTimeMillis();
        System.out.println("user开始" + x);
        List<User> list = new LinkedList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(new User(String.valueOf(i), String.valueOf(i + 1)));
        }
        List<User> merge = new ForestNodeMerger<User>().merge(list, User::getId, User::getPid, User::getChildren);
        long x1 = System.currentTimeMillis();
        System.out.println("user结束" + x1);
        System.out.println("user计算" + (x1 - x));
    }
}
