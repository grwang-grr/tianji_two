package com.tianji.learning.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.learning.domain.dto.CollectFormDTO;
import com.tianji.learning.domain.po.LessonCollect;
import com.tianji.learning.domain.query.LessonCollectQuery;
import com.tianji.learning.domain.vo.LessonCollectVO;

public interface ILessonCollectService extends IService<LessonCollect> {

    PageDTO<LessonCollectVO> queryMyCollects(LessonCollectQuery query);

    void addCollect(CollectFormDTO dto);

    Boolean isCollected(Long lessonId);
}
