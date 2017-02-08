package com.github.bingoohuang.blackcatdemo.controller;

import com.github.bingoohuang.blackcat.instrument.annotations.BlackcatMonitor;
import com.github.bingoohuang.blackcat.instrument.callback.Blackcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Random;

@Controller
//@BlackcatMonitor(debug = true)
public class MessageController {
    @Autowired InMemoryMessageRespository messageRepository;

    @RequestMapping("/")
    public ModelAndView list() {
        System.err.println("Nano:" + System.currentTimeMillis());
        Iterable<Message> messages = this.messageRepository.findAll();
        return new ModelAndView("messages/list", "messages", messages);
    }

    @RequestMapping("/fuck")
    public String fuck() {
        throw new RuntimeException("bingoo testing");
    }

    @RequestMapping("/hello")
    public String hello() {
        return "messages/hello";
    }

    @RequestMapping("/id/{id}")
    public ModelAndView view(@PathVariable("id") Message message) {
        return new ModelAndView("messages/view", "message", message);
    }

    @RequestMapping(value = "/", params = "form", method = RequestMethod.GET)
    public String createForm(@ModelAttribute Message message) {
        return "messages/form";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView create(@Valid Message message, BindingResult result,
                               RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("messages/form", "formErrors", result.getAllErrors());
        }
        message = this.messageRepository.save(message);
        redirect.addFlashAttribute("globalMessage", "Successfully created a new message");
        return new ModelAndView("redirect:/id/{message.id}", "message.id", message.getId());
    }

    @Autowired DemoService demoService;

    @ResponseBody
    @RequestMapping("/rest")
    public String rest() throws InterruptedException {
        Blackcat.log("step1");
        step1();
        step2();
        step3();
        Random random = new Random();
        Thread.sleep(random.nextInt(200));
        Blackcat.log("步骤2");
        Thread.sleep(random.nextInt(200));
        Blackcat.log("step3, 调用接口");
        demoService.service();

        return "rest response body";
    }

    @BlackcatMonitor
    private void step3() {
    }

    @BlackcatMonitor
    private void step2() {

    }

    @BlackcatMonitor
    private void step1() {

    }

    @RequestMapping("/foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }


    @ResponseBody
    @RequestMapping("/count")
    public String count() {
        Blackcat.count("demo.count");
        return "" + System.currentTimeMillis();
    }

    @ResponseBody
    @RequestMapping("/sum")
    public String sum() {
        int incr = new Random().nextInt(100);
        Blackcat.sum("demo.sum", incr);
        return "" + incr;
    }
}