package com.asiainfo.springboot.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年7月11日 下午3:39:42
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Controller
public class UploadController {
    
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @GetMapping("/upload")
    public String upload(HttpServletRequest request) {
        return "upload";
    }
    
    @PostMapping("/upload")
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, 
            HttpServletRequest request, Model model) {
        
        logger.info("upload submit()  is executed!");
        if (file.isEmpty()) {
            return "upload";
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        try {
            File dest = new File(path, fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
                dest.createNewFile();
            }
            file.transferTo(dest);
        } catch (Exception ex) {
            logger.error("error on transfer to target folder:{}\n{}", path, ex);
        }
        model.addAttribute("fileUrl", request.getContextPath() + "/upload/" + fileName);
        model.addAttribute("fileName", fileName);
        return "filelist";
    }
    
    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, 
            HttpServletRequest request, HttpServletResponse response) {
        
        logger.info("download()  is executed, fileName={}", fileName);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        String path = request.getSession().getServletContext().getRealPath("upload");
        
        InputStream in = null;
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            in = new FileInputStream(new File(path + File.separator + fileName));
            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
            }
            out.flush();
        } catch (Exception ex) {
            logger.error("error on transfer to client\n", ex);
        } finally {
            closeCliently(in, out);
        }
    }
    
    // 流关闭工具
    protected void closeCliently(AutoCloseable... closeables) {
        if (null != closeables) {
            for (AutoCloseable closeable : closeables) {
                try {
                    closeable.close();
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
    }
}
