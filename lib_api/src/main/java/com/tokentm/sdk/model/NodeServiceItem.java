package com.tokentm.sdk.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class NodeServiceItem implements Serializable {

    /**
     * did : string
     * doc : string
     * funcList : [{"desc":"string","name":"string","type":"string","uri":"string"}]
     * name : string
     * url : string
     */

    private String did;
    private String doc;
    private String name;
    private String url;
    private List<NodeFunction> funcList;

    public String getDid() {
        return did;
    }

    public String getDoc() {
        return doc;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<NodeFunction> getFuncList() {
        return funcList;
    }

    public static class NodeFunction implements Serializable {
        /**
         * desc : string
         * name : string
         * type : string
         * uri : string
         */

        private String desc;
        private String name;
        private String type;
        private String uri;

        public String getDesc() {
            return desc;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getUri() {
            return uri;
        }
    }
}
