package hello;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FormSubController {
    
    /**
     * 第一步在跳转到保存的表单界面的时候 先生成toke
     * 放到session里存储起来，用于后面的表单提交的时候，做比对，同时这个值要放到页面的隐藏域上
     * 下次提交表单的时候把这个隐藏域里的值也必须一起提交过来
     * 这个放到session里是否可以考虑用redis里扩展？因为在分布式的情况下，还要考虑分布式的seeson
     * */
    @RequestMapping("/testform")
    public String index(HttpServletRequest request) {
        String formToken = UUID.randomUUID().toString();//创建令牌
        System.out.println("在FormServlet中生成的token：" + formToken);
        request.getSession().setAttribute("formToken", formToken);  //在服务器使用session保存token(令牌)

        return "testForm";
    }
    /**
     * 第二步 对提交过来的表单进行验证
     * */
    @RequestMapping("/vaSubmiit")
    public String vaSubmiit(HttpServletRequest request) {
        boolean b = isRepeatSubmit(request);//判断用户是否是重复提交
        if(b==true){
            System.out.println("请不要重复提交--跳转一个提示的页面提示不允许重复提交");
            return "cfSumiit";
        }
        //------doSomeThing---处理允许提交的逻辑------
        // 处理完毕之后，一定记得要在session里移除这个key值，不然就发生重复提交了，关于这里，尽量考虑用redis来替换
        request.getSession().removeAttribute("token");//移除session中的token
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
        //取出存储在Session中的token
        String server_token = (String) request.getSession().getAttribute("formToken");
        //2、如果当前用户的Session中不存在Token(令牌)，则用户是重复提交了表单
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
