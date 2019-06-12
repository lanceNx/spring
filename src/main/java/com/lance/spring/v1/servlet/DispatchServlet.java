package com.lance.spring.v1.servlet;

import com.lance.spring.v1.annotation.Autowried;
import com.lance.spring.v1.annotation.Controller;
import com.lance.spring.v1.annotation.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Date: 2019/6/7
 * Author: Lance
 * Class action:lance-spring v 1.0
 */
public class DispatchServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private Map<String, Object> beanMap = new ConcurrentHashMap<String, Object>();

    private List<String> classNames = new ArrayList<String>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("开始调用 doPost - - - - - - -- - -- - -- - ");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //开始初始化的进程

        //开始定位
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        //加载配置文件中的内容，开始扫包
        doScanner(contextConfig.getProperty("scanPackage"));

        //实例化对象丢入ioc容器
        doAddIoc();

        //自动注入
        doAutowried();

    }

    private void doAutowried() {
        if(beanMap.isEmpty()) return;
        for (Map.Entry<String, Object> map : beanMap.entrySet()) {
            //获取ioc容器中的一个对象的所有成员变量
            Field[] fields = map.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                //判断是否有autowrited注解
                if(!field.isAnnotationPresent(Autowried.class)) continue;

                Autowried autowried = field.getAnnotation(Autowried.class);
                String beanName = autowried.value().trim();
                //如果autowried没有设置值 获取类型name
                if("".equals(beanName)) {
                    beanName = lowerFirstCase(field.getType().getName());
                }

                field.setAccessible(true);
                try {
                    //向obj对象的这个Field设置新值value
                    field.set(map.getValue(),beanMap.get(beanName));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void doAddIoc() {
        //如果类名列表为空 直接return
        if(classNames.isEmpty()) return;

        //遍历类名列表
        for (String className : classNames) {
            try {
                //通过全类名获取该类的字节码
                Class<?> clazz = Class.forName(className);

                if(!clazz.isAnnotationPresent(Controller.class) || !clazz.isAnnotationPresent(Service.class)) continue;

                //如果是controller注解
                if(clazz.isAnnotationPresent(Controller.class)) {
                    //bean首字母小写
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    //实例化
                    beanMap.put(beanName, clazz.newInstance());
                } else if(clazz.isAnnotationPresent(Service.class)) {
                    //判断service注解是否有设置值
                    Service service = clazz.getAnnotation(Service.class);
                    //设置默认beanName
                    String beanName = service.value();
                    //如果设置了值
                    if("".equals(beanName.trim())) {
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }
                    Object newInstance = clazz.newInstance();
                    //放入ioc容器
                    beanMap.put(beanName,newInstance);

                    //getInterfaces() 此方法返回这个类中实现接口的数组
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> face : interfaces) {
                        //以实现类名首字母小写放入ioc容器
                        String faceName = lowerFirstCase(face.getSimpleName());
                        beanMap.put(faceName,face.newInstance());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void doScanner(String scanPackage) {
        //获取包对应的路径
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll(".", "/"));
        //获取这个文件夹
        File dir = new File(url.getFile());
        //遍历文件夹下所有文件
        for (File file : dir.listFiles()) {
            //如果是文件夹 递归向下
            if(file.isDirectory())
                doScanner(scanPackage+file.getName());

            //如果不是文件夹 就是将其放入class列表集合
            classNames.add((scanPackage+file.getName()).replace(".class",""));
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        //获取到这个文件的流
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation.replace("classpath",""));

        //将其加载进入properties对象
        try {
            contextConfig.load(resource);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != resource) {
                try {
                    resource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 字符串首字母小写
     * @param str 需要转的字符串
     * @return 转好的字符串
     */
    private String lowerFirstCase(String str){
        char [] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
