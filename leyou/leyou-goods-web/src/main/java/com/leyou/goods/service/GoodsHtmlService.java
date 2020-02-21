package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


@Service
public class GoodsHtmlService {
    @Autowired
    private TemplateEngine engine;
    @Autowired
    private GoodsService goodsService;

    /**
     * 页面静态化
     * @param spuId
     */
    public void createHtml(Long spuId) {
        //初始化运行上下文
        Context context = new Context();
        //设置数据模型
        context.setVariables(goodsService.loadData(spuId));
        PrintWriter printWriter = null;
        try {
            //静态文件写入服务器本地
            File file = new File("E:\\Java\\nginx-1.14.0\\html\\item\\" + spuId + ".html");
            printWriter = new PrintWriter(file);
            //("模板名", context, writer);
            engine.process("item", context, printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (printWriter!=null) {
                printWriter.close();
            }

        }

    }

    public void deleteHtml(Long id) {
        File file = new File("E:\\Java\\nginx-1.14.0\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }
}