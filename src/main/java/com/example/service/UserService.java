package com.example.service;

import com.example.model.*;
import com.example.repository.SharedCalenderRepository;
import com.example.repository.SharedUserRepository;
import com.example.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final SharedCalenderRepository sharedCalenderRepository;
    private final SharedUserRepository sharedUserRepository;
    private final SharedUserService sharedUserService;
    private final ScheduleDataService scheduleDataService;

    @Autowired
    public UserService(UsersRepository usersRepository, SharedCalenderRepository sharedCalenderRepository, SharedUserRepository sharedUserRepository, SharedUserService sharedUserService, ScheduleDataService scheduleDataService) {
        this.usersRepository = usersRepository;
        this.sharedCalenderRepository = sharedCalenderRepository;
        this.sharedUserRepository = sharedUserRepository;
        this.sharedUserService = sharedUserService;
        this.scheduleDataService = scheduleDataService;
    }


    public boolean register(UsersDTO usersDTO) {
        // 유저 닉네임 중복 여부 검사
        if(usersRepository.findByUserNickname(usersDTO.getUserNickname()).isPresent()){
            return false;
        }

        // 중복이 아닌 경우 Users 테이블에 추가
        Users users = new Users();
        users.setUserName(usersDTO.getUserName());
        users.setPassword(usersDTO.getPassword());
        users.setUserNickname(usersDTO.getUserNickname());
        users.setUserAge(usersDTO.getUserAge());
        users.setUserSex(usersDTO.getUserSex());
        users.setUserPhone(usersDTO.getUserPhone());

        usersRepository.save(users);
        return true;
    }

    @Transactional
    public UsersDTO login(UsersDTO usersDTO) {
        Optional<Users> userOptional = usersRepository.findByUserNickname(usersDTO.getUserNickname());
        if(userOptional.isPresent()){
            usersDTO.setId(userOptional.get().getId());
            usersDTO.setUserName(userOptional.get().getUserName());
            usersDTO.setUserSex(userOptional.get().getUserSex());
            usersDTO.setUserAge(userOptional.get().getUserAge());
            usersDTO.setUserPhone(userOptional.get().getUserPhone());

            if(userOptional.get().getPassword().equals(usersDTO.getPassword())) {
                return usersDTO;
            } else {
                // 비밀번호 틀리면 false
                return null;
            }
        } else {
            // 유저(유저 닉네임)가 존재하지 않아도 false
            return null;
        }
    }

    public boolean delete(Long id) {
        if(usersRepository.existsById(id)){
            usersRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean update(UsersDTO usersDTO) {
        Optional<Users> usersOptional = usersRepository.findById(usersDTO.getId());
        if(usersOptional.isPresent()){
            usersOptional.get().setUserName(usersDTO.getUserName());
            usersOptional.get().setPassword(usersDTO.getPassword());
            usersOptional.get().setUserNickname(usersDTO.getUserNickname());
            usersOptional.get().setUserAge(usersDTO.getUserAge());
            usersOptional.get().setUserSex(usersDTO.getUserSex());
            usersOptional.get().setUserPhone(usersDTO.getUserPhone());
            if(usersRepository.findByUserNickname(usersDTO.getUserNickname()).isPresent()){
                return false;
            }
            usersRepository.save(usersOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public UsersDTO userInfo(Long id) {
        Optional<Users> usersOptional = usersRepository.findById(id);
        if(usersOptional.isPresent()){
            UsersDTO usersDTO = new UsersDTO();
            usersDTO.setId(usersOptional.get().getId());
            usersDTO.setPassword(usersOptional.get().getPassword());
            usersDTO.setUserNickname(usersOptional.get().getUserNickname());
            usersDTO.setUserName(usersOptional.get().getUserName());
            usersDTO.setUserAge(usersOptional.get().getUserAge());
            usersDTO.setUserSex(usersOptional.get().getUserSex());
            usersDTO.setUserPhone(usersOptional.get().getUserPhone());
            return usersDTO;
        } else {
            return null;
        }
    }


    // 캘린더 리스트 생성을 위한 캘린더 리스트 조회
    public List<SharedCalenderDTO> myCalenderList(Long userId) {
        // 어떤 유저의 캘린더 리스트를 조회할 것인지 찾고
        Optional<Users> usersOptional = usersRepository.findById(userId);
        if(usersOptional.isEmpty()){
            return null;
        }
        // 해당 유저의 식별자로 SharedUser에서 자신이 속한 캘린더의 식별자들을 가져오고
        List<SharedUser> sharedUserList = sharedUserRepository.findBySharedUser(usersOptional.get());
        if(sharedUserList.isEmpty()){
            return null;
        }

        List<SharedCalenderDTO> sharedCalenderDTOList = new ArrayList<>();
        // 얻어낸 캘린더 식별자를 통해 SharedCalender에 접근하여 캘린더 전체 정보를 얻는다.
        for (SharedUser sharedUser : sharedUserList) {
            SharedCalenderDTO sharedCalenderDTO = new SharedCalenderDTO();
            Optional<SharedCalender> tmp = sharedCalenderRepository.findById(sharedUser.getSharedCalender().getId());
            if(tmp.isPresent()){
                sharedCalenderDTO.setId(tmp.get().getId());
                sharedCalenderDTO.setCalenderName(tmp.get().getCalenderName());
                sharedCalenderDTO.setAdminUserId(tmp.get().getAdminUserId().getId());
                sharedCalenderDTO.setStartDate(tmp.get().getStartDate());
                sharedCalenderDTO.setEndDate(tmp.get().getEndDate());
                sharedCalenderDTOList.add(sharedCalenderDTO);
            }
        }


        if(sharedCalenderDTOList.isEmpty()) {
            return null;
        }
        return sharedCalenderDTOList;
    }

    public boolean processInvite(UsersDTO usersDTO, HttpSession httpSession) {
        // 로그인하는 시점에서는 유저 닉네임과 패스워드 정보만 알 수 있다.
        // 따라서 유저 식별자를 알기 위해서는 유저 닉네임을 사용해서 해당 유저를 데이터베이스에서 찾고, 유저 식별자를 알아내야 한다.
        Optional<Users> usersOptional = usersRepository.findByUserNickname(usersDTO.getUserNickname());
        if (usersOptional.isPresent()) {
            usersDTO.setId(usersOptional.get().getId());
        } else {
            // 유저 닉네임으로 유저를 찾지못하면 초대 실패
            return false;
        }

        if(httpSession.getAttribute("calenderId") != null) {
            Long calenderId = (Long) httpSession.getAttribute("calenderId");

            SharedUserDTO sharedUserDTO = new SharedUserDTO();
            sharedUserDTO.setSharedUserId(usersDTO.getId());
            sharedUserDTO.setSharedCalenderId(calenderId);
            sharedUserService.addUser(sharedUserDTO);


            // 사용을 완료한 세션은 제거
            httpSession.removeAttribute("calenderId");
            return true;
        } else {
            return false;
        }
    }
}