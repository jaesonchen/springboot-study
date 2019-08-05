package com.asiainfo.springboot.security.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月29日 下午3:54:58
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class ImageCodeGenerator {
    
    private ImageCodeProperties properties = new ImageCodeProperties();

    // 生成图形验证码
    public ImageCode generate(ServletWebRequest request) {
        
        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", properties.getWidth());
        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", properties.getHeight());
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        // 干扰线
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String code = "";
        int length = ServletRequestUtils.getIntParameter(request.getRequest(), "length", properties.getLength());
        // 验证码
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            code += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }

        g.dispose();
        return new ImageCode(image, code, properties.getExpireIn());
    }

    // 生成随机背景条纹
    private Color getRandColor(int fc, int bc) {
        
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
