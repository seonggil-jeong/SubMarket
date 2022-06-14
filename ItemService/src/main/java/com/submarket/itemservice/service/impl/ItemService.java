package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.dto.CategoryDto;
import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.jpa.ItemRepository;
import com.submarket.itemservice.jpa.entity.CategoryEntity;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.mapper.CategoryMapper;
import com.submarket.itemservice.mapper.ItemMapper;
import com.submarket.itemservice.service.IItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("ItemService")
public class ItemService implements IItemService {
    private final ItemRepository itemRepository;
    private final CategoryService categoryService;
    private final S3Service s3Service;

    @Override
    @Transactional
    public int saveItem(ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + ".saveItem Start");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategorySeq(itemDto.getCategorySeq());

        CategoryDto rDto = categoryService.findCategory(categoryDto);
        String subImagePath = "/";

        log.info("categoryName : " + rDto.getCategoryName());
        CategoryEntity categoryEntity = CategoryMapper.INSTANCE.categoryDtoToCategoryEntity(rDto);

        itemDto.setCategory(categoryEntity);

        itemDto.setItemStatus(1);
        itemDto.setReadCount20(0);
        itemDto.setReadCount30(0);
        itemDto.setReadCount40(0);
        itemDto.setReadCountOther(0);

        log.info("MainImageSize : " + itemDto.getMainImage().getSize());


        // 상품 이미지 등록 S3 Service (File, dirName) return : S3 Image Path
        /** Main Image 는 항상 NotNull */
        String mainImagePath = s3Service.uploadImageInS3(itemDto.getMainImage(), "images");

        if (itemDto.getSubImage() != null) {
        subImagePath = s3Service.uploadImageInS3(itemDto.getSubImage(), "images");
        }

        itemDto.setSubImagePath(subImagePath);
        itemDto.setMainImagePath(mainImagePath);

        // 이미지 정보가 없다면 Exception -> return "/" (기본 Image Path)

        log.info("" + itemDto.getCategory());
        ItemEntity itemEntity = ItemMapper.INSTANCE.itemDtoToItemEntity(itemDto);
        itemRepository.save(itemEntity);



        log.info(this.getClass().getName() + ".saveItem End");
        return 1;
    }

    @Override
    @Transactional
    public ItemDto findItemInfo(ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + "findItemInfo Start!");

        int itemSeq = itemDto.getItemSeq();

        log.info("itemSeq : " + itemSeq);

        Optional<ItemEntity> itemEntityOptional = itemRepository.findById(itemSeq);

        ItemDto rDto = ItemMapper.INSTANCE.itemEntityToItemDto(itemEntityOptional.get());

        if (rDto == null) {
            throw new RuntimeException("상품 정보를 찾을 수 없습니다");
        }

        log.info(this.getClass().getName() + ".findItemInfo End!");
        return rDto;
    }

    @Override
    public List<ItemDto> findAllItem() throws Exception {
        log.info(this.getClass().getName() + "findAllItem Start");

        List<ItemDto> itemDtoList = new LinkedList<>();
        Iterable<ItemEntity> itemEntityList = itemRepository.findAll();

        itemEntityList.forEach(itemEntity -> {
            itemDtoList.add(ItemMapper.INSTANCE.itemEntityToItemDto(itemEntity));
        });

        log.info(this.getClass().getName() + "findAllItem End");

        return itemDtoList;
    }

    @Override
    @Transactional
    public int offItem(ItemDto itemDto) throws Exception {
        // TODO: 2022-05-16 상품 활성화 여부 확인
        int itemSeq = itemDto.getItemSeq();

        itemRepository.offItemStatus(itemSeq);
        return 1;
    }

    @Override
    @Transactional
    public int onItem(ItemDto itemDto) throws Exception {
        int itemSeq = itemDto.getItemSeq();

        itemRepository.onItemStatus(itemSeq);
        return 1;
    }

    @Override
    @Transactional
    public int modifyItem(ItemDto itemDto) throws Exception {

        itemRepository.modifyItem(itemDto.getItemSeq(), itemDto.getItemContents(), itemDto.getItemPrice(),
                itemDto.getItemCount(), itemDto.getItemTitle());
        return 1;
    }

    @Override
    @Transactional
    public List<ItemDto> findItemBySellerId(String sellerId) throws Exception {
        // Seller 등록한 상품 조회
        log.info(this.getClass().getName() + "findItemBySellerId Start!");

        List<ItemDto> itemDtoList = new LinkedList<>();
        try {
            List<ItemEntity> itemEntityList = itemRepository.findAllBySellerId(sellerId);

            log.info("Repository End");
            List<ItemDto> finalItemDtoList = itemDtoList;
            itemEntityList.forEach(item -> {
                finalItemDtoList.add(ItemMapper.INSTANCE.itemEntityToItemDto(item));
            });

            itemDtoList = finalItemDtoList;


        } catch (HttpStatusCodeException statusCodeException) {
            int code = statusCodeException.getRawStatusCode();
            log.info(code + "(HttpStatusCodeException) : " + statusCodeException);
            itemDtoList = new LinkedList<>();

        } catch (Exception e) {
            log.info("Exception : " + e);
            itemDtoList = new LinkedList<>();
        } finally {
            log.info(this.getClass().getName() + "findItemBySellerId End!");
            return itemDtoList;

        }
    }

    @Override
    @Transactional
    @Async
    public void upCount(int itemSeq, int userAge) throws Exception {
        // 조회수 증가
        int cUserAge = 0;
        cUserAge += userAge;
        log.info("userAge : " + cUserAge);
        if (cUserAge > 0 && cUserAge <= 29) {
            itemRepository.increaseReadCount20(itemSeq);

        } else if (cUserAge >= 30 && cUserAge <= 39) {
            itemRepository.increaseReadCount30(itemSeq);
        } else if (cUserAge >= 40 && cUserAge <= 49) {
            itemRepository.increaseReadCount40(itemSeq);
        } else {
            itemRepository.increaseReadCountOther(itemSeq);
        }

        log.info(this.getClass().getName() + "upCount End");
    }

    @Override
    @Transactional
    @Async
    public void upCountCustom(int itemSeq, int userAge, int readValue) throws Exception {
        log.info(this.getClass().getName() + "upCountCustom Start!");

        if (userAge > 0 && userAge <= 29) {
            itemRepository.increaseCustomReadCount20(itemSeq, readValue);

        } else if (userAge >= 30 && userAge <= 39) {
            itemRepository.increaseCustomReadCount30(itemSeq, readValue);
        } else if (userAge >= 40 && userAge <= 49) {
            itemRepository.increaseCustomReadCount40(itemSeq, readValue);
        } else {
            itemRepository.increaseCustomReadCountOther(itemSeq, readValue);
        }


        log.info(this.getClass().getName() + "upCountCustom End!");

    }
}
