package com.hcloud.apm.analysis.bean;

import com.alibaba.fastjson.JSON;

/**
 * Created by 陈志民 on 2016/11/23.
 */
public class GenerateSQL {
    // @Test
    public void testGenerateSQL(){
        String json = "{\n" +
                "  \"labels\": [\n" +
                "    {\n" +
                "      \"labelName\": \"中年白领\",\n" +
                "      \"fields\": [\n" +
                "        {\n" +
                "          \"fieldName\": \"age\",\n" +
                "          \"operation\": \">\",\n" +
                "          \"param\": \"30\",\n" +
                "          \"relation\": \"null\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"fieldName\": \"salary\",\n" +
                "          \"operation\": \">\",\n" +
                "          \"param\": \"20000\",\n" +
                "          \"relation\": \"and\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"labelName\": \"低薪青年\",\n" +
                "      \"fields\": [\n" +
                "        {\n" +
                "          \"fieldName\": \"salary\",\n" +
                "          \"operation\": \">\",\n" +
                "          \"param\": \"1000\",\n" +
                "          \"relation\": \"null\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"fieldName\": \"age\",\n" +
                "          \"operation\": \"<\",\n" +
                "          \"param\": \"53\",\n" +
                "          \"relation\": \"and\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        LabelsDefine lists = JSON.parseObject(json,LabelsDefine.class);

        for(LabelBeanJava lb:lists.getLabels()){
            StringBuilder sb = new StringBuilder("where ");
            for(FieldJava fd :lb.getFields()){

                if(!fd.relation.equals("null")){
                        sb.append(" ");

                    sb.append(fd.getRelation());
                    sb.append(" ");
                }
                sb.append(fd.getFieldName()+fd.getOperation() +fd.getParam());

            }
            System.out.println(sb.toString());
         }

    }
}
