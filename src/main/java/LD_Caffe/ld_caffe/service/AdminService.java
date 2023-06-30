package LD_Caffe.ld_caffe.service;

import LD_Caffe.ld_caffe.domain.MenuEntity;
import LD_Caffe.ld_caffe.domain.UserEntity;
import LD_Caffe.ld_caffe.dto.MenuDto;
import LD_Caffe.ld_caffe.repository.MenuRepository;
import LD_Caffe.ld_caffe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.event.MenuEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Array;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    //유저 이름 가져오기
    public ArrayList<String> getUserNames(){

        List<UserEntity> userInfo = userRepository.findAll();
        ArrayList<String> userNames = new ArrayList<>();

        for (UserEntity i : userInfo){
            userNames.add(i.getUserName());
        }

        return userNames;
    }


    public Integer deleteUser(String userName){
        System.out.println(userName);
        // userName로 userId찾아서 그값으로 데이터 삭제
        Optional<UserEntity> userInfo = userRepository.findByuserName(userName);
//        List<UserEntity> userInfo = userRepository.findByuserName(userName);
        if (userInfo.isPresent()){
            System.out.println("데이터베이스에서 유저명을 찾았습니다.");
            String userId = userInfo.get().getUserId();
            userRepository.deleteById(userId);
            return 1;
        }else {
            System.out.println("데이터베이스에서 해당 유저명이 없습니다.");
            return 0;
        }
    }

    public void addMenu(MenuDto menuDto){

        MenuEntity menu = MenuEntity.toMenuEntity(menuDto);
        menuRepository.save(menu);
    }

    public List<MenuDto> getAllMenuInfo() throws IOException {
        List<MenuEntity> menuInfoList = menuRepository.findAll();
        List<MenuDto> menuInfo = new ArrayList<>();

        for (MenuEntity menu : menuInfoList){

//            System.out.println(menu.getMenuImagePath());

            MenuDto menuDto = new MenuDto();
            menuDto.setCategory(menu.getMenuCategory());
            menuDto.setName(menu.getMenuName());
            menuDto.setContent(menu.getMenuContents());
            menuDto.setPrice(menu.getMenuPrice());

            //경로를 가져와서 이미지를 바이트값으로 바꿔서 DTO에 해당 이미지에 대한 바이트값 넣어주기
            String imagePath = menu.getMenuImagePath();
            File file = new File(imagePath);
            byte[] imageBytes = Files.readAllBytes(file.toPath());


            //이미지 바이트를 Base64로 인코딩하고 Dto에 추가
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
            menuDto.setImageBytes(encodedImage.getBytes());
//            menuDto.setImageType("MediaType.IMAGE_JPEG");




            menuInfo.add(menuDto);
        }

            return menuInfo;
        }


}