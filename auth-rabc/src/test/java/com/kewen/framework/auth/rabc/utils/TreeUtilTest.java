package com.kewen.framework.auth.rabc.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TreeUtilTest {

    @Test
    public void removeIfUnmatch() {
        String str = getStr();
        List<TTree> tTrees = JSONObject.parseArray(str, TTree.class);
        TreeUtil.removeIfUnmatch(tTrees,t->{
          return   (t.getId()==111);
        });
        assertEquals(3, tTrees.size());
        assertTrue(CollectionUtils.isEmpty(tTrees.get(0).getChildren().get(0).getChildren()));
        tTrees = JSONObject.parseArray(str, TTree.class);
        TreeUtil.removeIfUnmatch(tTrees,t->{
            return   !(t.getId()==111);
        });
        assertEquals(1, tTrees.size());
    }

    @Data
    public static class TTree implements TreeUtil.TreeBase<TTree,Long>{
        private Long id;
        private Long parentId;
        private List<TTree> children;

        public TTree() {

        }

        public TTree(Long id, Long parentId) {
            this.id = id;
            this.parentId = parentId;
        }
    }
    private String getStr(){
        return "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"parentId\": 0,\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"id\": 11,\n" +
                "        \"parentId\": 1,\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"id\": 111,\n" +
                "            \"parentId\": 11\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"parentId\": 0,\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"id\": 22,\n" +
                "        \"parentId\": 2,\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"id\": 222,\n" +
                "            \"parentId\": 22\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 3,\n" +
                "    \"parentId\": 0,\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"id\": 33,\n" +
                "        \"parentId\": 3,\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"id\": 333,\n" +
                "            \"parentId\": 33\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";
    }
}