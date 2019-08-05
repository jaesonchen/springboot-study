package com.asiainfo.springboot.security.image;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月29日 下午3:44:44
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@RestController
public class ValidateController {
    
    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";
    
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    private ImageCodeGenerator generator = new ImageCodeGenerator();

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        ImageCode imageCode = generator.generate(new ServletWebRequest(request));
        // request.getSession().setAttribute(SESSION_KEY, imageCode);
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }
}
