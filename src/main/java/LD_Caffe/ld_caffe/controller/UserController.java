package LD_Caffe.ld_caffe.controller;


import LD_Caffe.ld_caffe.domain.CardEntity;
import LD_Caffe.ld_caffe.domain.UserEntity;
import LD_Caffe.ld_caffe.dto.DeleteDto;
import LD_Caffe.ld_caffe.dto.UserDto;
import LD_Caffe.ld_caffe.repository.CardRepository;
import LD_Caffe.ld_caffe.repository.UserRepository;
import LD_Caffe.ld_caffe.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

   public final UserInfoService userInfoService;
   public final CardRepository cardRepository;
   public final UserRepository userRepository;

//   public final TestService testService;

    @GetMapping("/error")
    public String error(){
        return "error";
    }
    @GetMapping("/userInfo")
    public String toUserInfo(Model model){

        List<UserEntity> userData = userRepository.findAll();
        List<CardEntity> cardData = new ArrayList<>();
//      List<UserEntity> userData = userInfoService.getAllUser();
//      System.out.println(userData.get(0).getUserName());

       for (UserEntity u : userData ){
           Integer cardNum = u.getUserCardNum();
//           System.out.println(cardNum);
           cardData.add(cardRepository.findByCardNumber(cardNum).get(0));

       }

       System.out.println((cardData));

       model.addAttribute("userData",userData);
       model.addAttribute("cardData",cardData);

        return "getUser";
    }

    @PostMapping("/userInfo")
    public RedirectView getUserInfo(@ModelAttribute UserDto userDto){

        System.out.println(userDto);
        userInfoService.save(userDto);


        return new RedirectView("/userInfo");
    }


    @PostMapping("/deleteUser")
    public RedirectView deleteUserInfo(@ModelAttribute DeleteDto deleteDto, RedirectAttributes redirectAttributes){
        String userId = deleteDto.getUserId();
        Optional<UserEntity> result = userRepository.findById(userId);
        if (result.isPresent()){

            //Optional에서 카드 값만 가져오기
            Integer cardNum = result.get().getUserCardNum();

            //User테이블과 Card테이블에서 값 지우기
            userInfoService.DeleteUser(userId);
            userInfoService.deleteCard((cardNum));

            System.out.println("UserId => " + userId + " (삭제완료!)");

            return new RedirectView("/userInfo");

        }else{
            return new RedirectView("/error");
        }

    }


    // <<<TEST>>>
//    @GetMapping("/test")
//    public String testGet(){
//
//        return "test";
//    }
//
//    @PostMapping("/test")
//    public String testPost(@ModelAttribute TestDto testDto){
//        System.out.println(testDto);
//        testService.save(testDto);
//        return "test";
//    }

}
