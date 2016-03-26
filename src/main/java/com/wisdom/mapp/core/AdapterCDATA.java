package com.wisdom.mapp.core;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * jaxb 属性 增加 CDATA标签
 * @author mler
 *
 */
public class AdapterCDATA extends XmlAdapter<String, String> {

	@Override
    public String marshal(String arg0) throws Exception {
        return "<![CDATA[" + arg0 + "]]>";
    }
    @Override
    public String unmarshal(String arg0) throws Exception {
        return arg0;
    }

}
