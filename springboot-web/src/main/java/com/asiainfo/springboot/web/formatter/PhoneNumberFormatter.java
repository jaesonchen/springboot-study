package com.asiainfo.springboot.web.formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import com.asiainfo.springboot.web.model.PhoneNumber;

/**   
 * @Description: string -> PhoneNumber 格式化
 * 
 * @author chenzq  
 * @date 2019年7月11日 下午1:48:56
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class PhoneNumberFormatter implements Formatter<PhoneNumber> {

    final Logger logger = LoggerFactory.getLogger(getClass());
    final Pattern pattern = Pattern.compile("^(\\d{3,4})-(\\d{7,8})$");
    
    @Override
    public String print(PhoneNumber object, Locale locale) {
        
        logger.info("print: {}", object);
        if (object == null) {
            return "";
        }
        return new StringBuilder()
                .append(object.getAreaCode())
                .append("-")
                .append(object.getPhoneNumber())
                .toString();
    }

    @Override
    public PhoneNumber parse(String text, Locale locale) throws ParseException {
        
        logger.info("parse: {}", text);
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setAreaCode(matcher.group(1));
            phoneNumber.setPhoneNumber(matcher.group(2));
            return phoneNumber;
        }
        throw new ParseException(String.format("类型转换失败，需要格式[010-12345678]，但格式是[%s]", text), 0);
    }
}
