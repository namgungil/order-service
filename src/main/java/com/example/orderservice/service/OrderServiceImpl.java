package com.example.orderservice.service;

import com.example.orderservice.dao.OrderDAO;
import com.example.orderservice.domain.*;
import com.example.orderservice.service.producer.OrderStringProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderDAO orderDAO;
    private final ModelMapper modelMapper;
    private final OrderStringProducer producer;

    @Override
    @Transactional
    public void save(OrderRequestDTO orderRequestDTO) {
        log.info("Saving order request: {}", orderRequestDTO);
        List<OrderProductEntity> datadetaillist =
                orderRequestDTO.getOrderDetailDTOList().stream()
                        .map((detailDTO) ->{
                            return modelMapper.map(detailDTO, OrderProductEntity.class);
                        }).collect(Collectors.toList());

        log.info("-----------");
        log.info("Saving order detail list: {}", datadetaillist);

        // 주문생성
        // 양방향관계인 경우 부모테이블과 자식테이블의 데이터를 한 번에 저장할 수 있다.
        // 부모테이블에 레코드를 저장할때 자식테이블의 레코드를 한 번에 같이 진행할 수 있다
        OrderEntity orderEntity = OrderEntity.makeOrderEntity(orderRequestDTO.getAddr(),
                orderRequestDTO.getCustomerId(),datadetaillist);
        orderDAO.save(orderEntity);

//        OrderEntity order =  modelMapper.map(orderRequestDTO, OrderEntity.class);
//        orderDAO.save(datadetaillist);

        // 주문이 성공하면 주문한 정보를 produck-service로 보내기
        // => 주문정보를 하나씩 꺼내서 넘기기
        // => 주문정보를 한꺼번에 넘기기 - 미션

        for (OrderDetailDTO product : orderRequestDTO.getOrderDetailDTOList()) {
            log.info("주문 성공한 상품 => {}", product);
            producer.sendOrder(product);
        }


    }

    @Override
    public OrderResponseDTO findById(Long orderId) {
        return null;
    }

    @Override
    public List<OrderResponseDTO> findAllByCustomerId(Long customerId) {
        return List.of();
    }
}
