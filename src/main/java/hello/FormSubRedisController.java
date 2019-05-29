package hello;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FormSubRedisController {
    
    private static String FORMTOKE = "form_token";
    @Autowired
    @Qualifier("forObject")
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 基于redis的放置表单重复提交的demo
     * */
    @RequestMapping("/testRedisform")
    public String index(HttpServletRequest request) {
        String formToken = UUID.randomUUID().toString();//创建令牌
        
        System.out.println("在FormServlet中生成的formToken：" + formToken);
        
        // 使用redis来存储这个 令牌
        redisTemplate.opsForValue().set(FORMTOKE, formToken);
        
        // 这里仍然借助session 把值方法页面上。当然前后端分离的情况下，这个值，直接可以通过返回值的dto 返回给客户端，无须用到seesion
        request.getSession().setAttribute("formToken", formToken);  //在服务器使用session保存token(令牌)
        return "testForm";
    }
    /**
     * 第二步 对提交过来的表单进行验证
     * */
    @RequestMapping("/vaRedisSubmiit")
    public String vaSubmiit(HttpServletRequest request) {
        boolean b = isRepeatSubmit(request);//判断用户是否是重复提交
        if(b==true){
            System.out.println("请不要重复提交--跳转一个提示的页面提示不允许重复提交");
            return "cfSumiit";
        }
        //------doSomeThing---处理允许提交的逻辑------
        // 处理完毕之后，一定记得要在redis里移除这个key值，不然就发生重复提交了
        redisTemplate.delete(FORMTOKE); //移除redis中的token
        System.out.println("否则提示成功---处理用户提交请求！！--比如跳转到提交成功的页面");
        return "formSucc";
    }
    /**
     * 判断客户端提交上来的令牌和服务器端生成的令牌是否一致
     * @param request
     * @return 
     *         true 用户重复提交了表单 
     *         false 用户没有重复提交表单
     */
    private boolean isRepeatSubmit(HttpServletRequest request) {
        String client_token = request.getParameter("formToken");
        //1、如果用户提交的表单数据中没有token，则用户是重复提交了表单
        if(client_token==null){
            return true;
        }
        //取出存储在redis中的token
        String server_token = (String) redisTemplate.opsForValue().get(FORMTOKE);
        //2、如果redis里的中不存在Token(令牌)，则用户是重复提交了表单
        if(server_token==null){
            return true;
        }
        //3、存储在Session中的Token(令牌)与表单提交的Token(令牌)不同，则用户是重复提交了表单
        if(!client_token.equals(server_token)){
            return true;
        }
        return false;
    }

}
