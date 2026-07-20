package com.tianji.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianji.api.client.trade.TradeClient;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.enums.UserType;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.exceptions.UnauthorizedException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.RandomUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.user.constants.UserConstants;
import com.tianji.user.domain.dto.StudentFormDTO;
import com.tianji.user.domain.dto.StudentUpdateDTO;
import com.tianji.user.domain.dto.StudentUpdatePasswordDTO;
import com.tianji.user.domain.po.User;
import com.tianji.user.domain.po.UserDetail;
import com.tianji.user.domain.query.UserPageQuery;
import com.tianji.user.domain.vo.StudentPageVo;
import com.tianji.user.service.IStudentService;
import com.tianji.user.service.IUserDetailService;
import com.tianji.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 学员详情表 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-12
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService {

    private final IUserService userService;
    private final IUserDetailService detailService;
    private final TradeClient tradeClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveStudent(StudentFormDTO studentFormDTO) {
        // 1.新增用户账号
        User user = new User();
        user.setCellPhone(studentFormDTO.getCellPhone());
        user.setPassword(studentFormDTO.getPassword());
        user.setType(UserType.STUDENT);
        userService.addUserByPhone(user, studentFormDTO.getCode());

        // 2.新增学员详情
        UserDetail student = new UserDetail();
        student.setId(user.getId());
        student.setName(RandomUtils.randomString(8));
        student.setRoleId(UserConstants.STUDENT_ROLE_ID);
        detailService.save(student);
    }

    @Override
    public void updateMyPassword(StudentFormDTO studentFormDTO) {
        userService.updatePasswordByPhone(
                studentFormDTO.getCellPhone(), studentFormDTO.getCode(), studentFormDTO.getPassword()
        );
    }

    @Override
    public PageDTO<StudentPageVo> queryStudentPage(UserPageQuery query) {
        // 1.分页条件
        Page<UserDetail> page  =  detailService.queryUserDetailByPage(query, UserType.STUDENT);
        List<UserDetail> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }

        // 2.查询购买的课程数量
        List<Long> stuIds = records.stream().map(UserDetail::getId).collect(Collectors.toList());
        Map<Long, Integer> numMap = tradeClient.countEnrollCourseOfStudent(stuIds);

        // 3.处理vo
        List<StudentPageVo> list = new ArrayList<>(records.size());
        for (UserDetail r : records) {
            StudentPageVo v = BeanUtils.toBean(r, StudentPageVo.class);
            list.add(v);
            v.setCourseAmount(numMap.get(r.getId()));
        }
        return new PageDTO<>(page.getTotal(), page.getPages(), list);
    }

    @Override
    public void updateStudent(StudentUpdateDTO studentUpdateDTO) {
//        Long currentUserId = UserContext.getUser();
//        if (currentUserId == null) {
//            throw new UnauthorizedException("未登录或登录已过期");
//        }
//        studentUpdateDTO.setId(currentUserId);
//        if (studentUpdateDTO.getId() == null) {
//            throw new BadRequestException("用户ID不能为空");
//        }
        if(!studentUpdateDTO.getId().equals(UserContext.getUser())){
            throw new BizIllegalException("只能修改自己的信息！");
        }
        UserDTO dto = BeanUtils.copyProperties(studentUpdateDTO, UserDTO.class);
        userService.updateUser(dto);
    }

    @Override
    public void updatePassword(StudentUpdatePasswordDTO dto) {
        if(!dto.getId().equals(UserContext.getUser())){
            throw new BizIllegalException("只能修改自己的信息！");
        }
        User user = userService.getById(dto.getId());
        if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())){
            throw new BizIllegalException("原密码错误！");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userService.updateById(user);
    }

}
