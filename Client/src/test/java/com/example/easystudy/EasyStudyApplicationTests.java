package com.example.easystudy;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.easystudy.Util.Captcha;
import com.example.easystudy.Util.JwtTokenUtil;
import com.example.easystudy.Util.TokenUtil;
import com.example.easystudy.dao.*;
import com.example.easystudy.pojo.*;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class EasyStudyApplicationTests {


    UserDao userDao;

    NoteDao noteDao;

    TypeDao typeDao;

    PlanDao planDao;

    NtopDao ntopDao;
    @Resource
    StringRedisTemplate  stringRedisTemplate;
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setNoteDao(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Autowired
    public void setTypeDao(TypeDao typeDao) {
        this.typeDao = typeDao;
    }

    @Autowired
    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }

    @Autowired
    public void setNtopDao(NtopDao ntopDao) {
        this.ntopDao = ntopDao;
    }

    //注册功能测试类
    //运行通过
    //薛永淇
    @Test
    public void RegisterTest(){

        System.out.println("用户test注册成功！");
        System.out.println((userDao.SelectUserByUserId("13550268269").toString()));

        /*User user = new User();
        user.setUserId("13550268269");
        user.setUserPassword("123");
        user.setUserEmail("1608858286@qq.com");
        user.setUserName("Test");
        int result = userDao.registerUser(user);
        if (result != 0){
            System.out.println("用户test注册成功！");
            System.out.println((userDao.selectUser(user.getUserId()).toString());
        }else{
            System.out.println("插入失败，测试未通过");
        }*/
    }

    //通过uid获取用户信息测试类
    //运行通过
    //薛永淇
    @Test
    public void GetUserInfoByUidTest(){
        int uId = 1;
        System.out.println("uId为 "+uId+" 的用户的基本信息为：");
        System.out.println(userDao.selectUserinfo(uId).toString());
    }

    //更新用户信息
    //运行通过
    //多次set中间不能用and链接
    //薛永淇
    @Test
    public void UpdateUserBasicInfoByUidTest(){
        int uId = 1;
        User user = new User();
        user.setuId(uId);
        user.setUserName("西岳雪");
        user.setUserAddress("四川省成都市锦江区");
        user.setUserEmail("123@qq.com");
        int result = userDao.updateUser(user);
        if (result != 0){
            System.out.println("修改基础信息成功");
            System.out.println(userDao.selectUserinfo(uId).toString());
        }else {
            System.out.println("修改失败，测试未通过");
        }
    }

    //修改密码测试类
    //运行通过
    //薛永淇
    @Test
    public void UpdateUserPasswordByUidTest(){
        int uId = 1;
        String oldPsw = "123";
        String newPsw = "123456789";
        if(userDao.selectUserinfo(uId).getUserPassword().equals(oldPsw)){
            System.out.println("老密码输入正确");
            userDao.updatePassword(uId, newPsw);
            System.out.println("新密码为：" + userDao.selectUserinfo(uId).getUserPassword());
        }else {
            System.out.println("老密码不对");
        }
    }

    //在系统首页展示用户上传笔记数与上传计划数消息测试类
    //运行通过
    //薛永淇
    @Test
    public void ShowCountAtHomePageByUid(){
        int uId = 1;
        Map<String,Integer> map = new HashMap<>();
        int uploadNotesNum = userDao.GetUploadNotesNumByUid(uId);//上传笔记数
        int createdPlansNum = userDao.GetCreatedPlansNumByUid(uId);//上传计划数

        map.put("uploadNotesNum", uploadNotesNum);
        map.put("createdPlansNum", createdPlansNum);

        System.out.println("uploadNotesNum：" + map.get("uploadNotesNum"));
        System.out.println("createdPlansNum：" + map.get("createdPlansNum"));
    }

    //在系统首页获取用户笔记列表
    //运行通过
    //薛永淇
    /*@Test
    public void ShowNotesAtHomePageByUid(){
        int uId = 1;
        List<Note> notesList = noteDao.GetAllNoteByUid(uId);
        for (Note note : notesList) {
            System.out.println(note.toString());
        }
    }*/

    //在系统首页获取今日待办
    //运行通过
    //薛永淇
    @Test
    public void ShowUnfinishedPlansAtHomePageByUidTest(){
        int uId = 1;
        List<Plan> unfinishedPlansList = planDao.GetUnfinishedPlansByUid(uId);
        for (Plan plan : unfinishedPlansList) {
            System.out.println("未完成计划：\n"+plan.toString());
        }
    }

    //获取用户type集合
    //运行通过
    //薛永淇
    @Test
    public void GetTypeListByUidTest(){
        int uId = 1;
        List<Type> typesList = typeDao.selectTypeListByUid(uId);
        for (Type type : typesList) {
            System.out.println(type.toString());
        }
    }

    //更新type信息测试类
    //运行通过
    //薛永淇
    @Test
    public void UpdateTypeByTidTest(){
        int tId = 1;
        int uId = 1;
        String typeName = "java";
        Type type = new Type();
        type.setuId(uId);
        type.settId(tId);
        type.setTypeName(typeName);

        List<Type> types = typeDao.selectTypeListByUid(type.uId);
        int flag = 1;
        for (Type type1:types) {
            if (type1.typeName.equals(type.typeName) && type1.tId!=type.tId)
            {
                System.out.println("名称重复");
                flag = 0;
            }
        }
        if (flag==1) {
            typeDao.updateTypeByTid(type.tId, type.typeName);
            System.out.println("修改成功，修改过后的名称为"+ typeDao.SelectTypeByTid(tId).getTypeName());
        }
    }


    @Test
    public void test1()
    {
        System.out.println(userDao.selectAllUser());
        System.out.println(userDao.selectUser("18008052868"));
        User user = new User();
        user.setUserName("小薛老师");
        user.setUserId("18008052868");
        System.out.println(userDao.updateUser(user));
    }

    //上传笔记数获取测试类
    //运行通过
    //薛永淇
    @Test
    public void GetUploadNotesNumByUidTest(){
        int uId = 1;
        System.out.println("uId为 "+uId+" 的用户上传的笔记数为" +
                "："+userDao.GetUploadNotesNumByUid(uId));
    }

    //创建计划数获取测试类
    //运行通过
    //薛永淇
    @Test
    public void GetCreatedPlansNumByUidTest(){
        int uId = 1;
        System.out.println("uId为 "+uId+" 的用户创建的计划数为："+userDao.GetCreatedPlansNumByUid(uId));
    }

    //根据uid查询用户所有笔记测试类
    //运行通过
    //薛永淇
    /*@Test
    public void GetAllNoteByUidTest(){
        int uId = 1;
        List<Note> notes = noteDao.GetAllNoteByUid(uId);
        for (Note note : notes) {
            System.out.println(note.toString());
        }
    }*/

    //查询笔记详情信息测试类
    //运行通过
    //薛永淇
    @Test
    public void ShowNoteInfoByNidTest(){
        int nId = 1;
        System.out.println(noteDao.ShowNoteInfoByNid(nId).toString());
    }

    //获取学习进度数据测试类
    //运行通过
    //薛永淇
    @Test
    public void GetUserStudyProcessByUidTest(){
        int uId = 1;
        System.out.println("完成计划有："+planDao.selectFNumber(uId));
        System.out.println("未完成计划有："+planDao.selectUNumber(uId));
        System.out.println("已复习笔记有："+noteDao.selectRNumber(uId));
        System.out.println("未复习笔记有："+noteDao.selectWNumber(uId));
    }

    //查询用户所有笔记测试类
    //运行通过
    //薛永淇
    /*@Test
    public void GetAllPlansByUidTest(){
        int uId = 1;
        List<Plan> plans = planDao.GetAllPlansByUid(uId);
        for (Plan plan : plans) {
            System.out.println(plan.toString());
        }
    }*/
    @Test
    public void deleteNoteByTid()
    {
        int tId = 1;
        noteDao.deleteNoteByTid(tId);
    }
    @Test
    public void addType()
    {
        Type type = new Type();
        type.setTypeName("c#");
        type.setuId(1);
        typeDao.addType(type);
    }
    /*@Test
    public void SelectNotesByKeyword()
    {
        String noteName = "JAVA";
        List<Note> notes = noteDao.SelectNotesByKeyword(noteName,1);
        System.out.println(notes);
    }*/
    @Test
    public void RemoveNotesFromPlan()
    {
        System.out.println(ntopDao.RemoveNotesFromPlan(1,2));
    }

    @Test
    public void AddNotesToPlan()
    {
        List<String> nIds = new ArrayList<>();
        nIds.add("1");
        nIds.add("2");
        int i;
        for (String nId:nIds) {
            i = Integer.parseInt(nId);
            if (i==1)
            {
                System.out.println("nid是"+i);
            }
        }
    }
    @Test
    public void ShowNidByPid()
    {
        List<Integer> nIds = ntopDao.ShowNidByPid(1);
        for (Integer nId:nIds) {
            System.out.println(noteDao.ShowNoteInfoByNid(nId));
        }
        System.out.println(nIds);
    }

    @Test
    public void test()
    {
        File file = new File("");
        System.out.println(file.getAbsolutePath());
    }

    @Test
    void contextLoads() {
        Captcha captcha = new Captcha();
        captcha.makeCaptcha();
        stringRedisTemplate.opsForValue().set("code",captcha.getCode());
        System.out.println(stringRedisTemplate.opsForValue().get("1"));
    }
    @Test
    void testRequest()
    {
//        String token_prefix = TokenUtil.token_prefix;
//        System.out.println(token_prefix);
        UserDto userDto = new UserDto();
        userDto.setUserId("wangyu");
        userDto.setUserPassword("1234567");
        String token = JwtTokenUtil.createToken(userDto);
        System.out.println(token);
        String token1 = "awdadwabvewfsgertfs";
        System.out.println(JwtTokenUtil.getUserId(token));
        System.out.println(JwtTokenUtil.verify(token1));
    }

}
