package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.client.UserServiceClient;
import com.submarket.itemservice.dto.CategoryDto;
import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.exception.ItemException;
import com.submarket.itemservice.exception.result.ItemExceptionResult;
import com.submarket.itemservice.jpa.CategoryRepository;
import com.submarket.itemservice.jpa.ItemRepository;
import com.submarket.itemservice.jpa.entity.CategoryEntity;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.mapper.CategoryMapper;
import com.submarket.itemservice.mapper.ItemMapper;
import com.submarket.itemservice.service.CategoryService;
import com.submarket.itemservice.service.ItemService;
import com.submarket.itemservice.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.submarket.itemservice.constants.S3Constants.IMAGE_FOLDER_PATH;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryService categoryServiceImpl;
    private final S3Service s3ServiceImpl;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(rollbackOn = ItemException.class)
    public void saveItem(ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + ".saveItem Start");


        CategoryDto rDto = categoryServiceImpl.findCategory(CategoryDto.builder()
                .categorySeq(itemDto.getCategorySeq()).build());


        String subImagePath = "/";

        log.info("categoryName : " + rDto.getCategoryName());
        CategoryEntity categoryEntity = CategoryMapper.INSTANCE.categoryDtoToCategoryEntity(rDto);

        itemDto.setCategory(categoryEntity);

        itemDto = setReadCountDefaultInItemDto(itemDto);

        log.info("MainImageSize : " + itemDto.getMainImage().getSize());


        // 상품 이미지 등록 S3 Service (File, dirName) return : S3 Image Path
        /** Main Image 는 항상 NotNull */
        final String mainImagePath = s3ServiceImpl.uploadImageInS3(itemDto.getMainImage(), IMAGE_FOLDER_PATH);

        if (itemDto.getSubImage() != null) {
            subImagePath = s3ServiceImpl.uploadImageInS3(itemDto.getSubImage(), IMAGE_FOLDER_PATH);
        }

        itemDto.setSubImagePath(subImagePath);
        itemDto.setMainImagePath(mainImagePath);

        // 이미지 정보가 없다면 Exception -> return "/" (기본 Image Path)

        log.info("" + itemDto.getCategory());
        ItemEntity itemEntity = ItemMapper.INSTANCE.itemDtoToItemEntity(itemDto);
        itemRepository.save(itemEntity);


        log.info(this.getClass().getName() + ".saveItem End");
    }

    private ItemDto setReadCountDefaultInItemDto(final ItemDto itemDto) {
        itemDto.setItemStatus(1);
        itemDto.setReadCount20(0);
        itemDto.setReadCount30(0);
        itemDto.setReadCount40(0);
        itemDto.setReadCountOther(0);

        return itemDto;
    }

    @Override
    public ItemDto findItemInfo(ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + "findItemInfo Start!");

        int itemSeq = itemDto.getItemSeq();

        log.info("itemSeq : " + itemSeq);

        Optional<ItemEntity> itemEntityOptional = itemRepository.findById(itemSeq);

        log.info(this.getClass().getName() + ".findItemInfo End!");


        return ItemMapper.INSTANCE.itemEntityToItemDto(itemEntityOptional.orElseThrow(
                () -> new ItemException(ItemExceptionResult.ITEM_NOT_FOUND)));
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
    @Transactional(rollbackOn = Exception.class)
    public int offItem(ItemDto itemDto) throws Exception {
        int itemSeq = itemDto.getItemSeq();

        itemRepository.offItemStatus(itemSeq);
        return 1;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public int onItem(ItemDto itemDto) throws Exception {
        int itemSeq = itemDto.getItemSeq();

        itemRepository.onItemStatus(itemSeq);
        return 1;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void modifyItem(final ItemDto itemDtoReq) throws Exception {
        log.info(this.getClass().getName() + ".modifyItem Start!");
        int itemSeq = itemDtoReq.getItemSeq();

        ItemEntity itemEntity = itemRepository.findById(itemSeq)
                .orElseThrow(() -> new ItemException(ItemExceptionResult.ITEM_NOT_FOUND));


        if (itemDtoReq.getMainImage() != null) {
            String mainImagePath = s3ServiceImpl.uploadImageInS3(itemDtoReq.getMainImage(), "images");
            itemEntity.setMainImagePath(mainImagePath);
        }

        if (itemDtoReq.getSubImage() != null) {
            String subImagePath = s3ServiceImpl.uploadImageInS3(itemDtoReq.getSubImage(), "images");
            itemEntity.setSubImagePath(subImagePath);
        }

        Optional<CategoryEntity> category = categoryRepository.findById(itemDtoReq.getCategorySeq());

        itemEntity.setItemTitle(itemDtoReq.getItemTitle());
        itemEntity.setItemPrice(itemDtoReq.getItemPrice());
        itemEntity.setItemCount(itemDtoReq.getItemCount());
        itemEntity.setItemContents(itemDtoReq.getItemContents());
        itemEntity.setCategory(category.get());

        itemRepository.save(itemEntity);

        log.info(this.getClass().getName() + ".modifyItem End!");
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


    /**
     * upReadCount
     *
     * @param itemSeq
     * @param userAge ** @param readValue -> 해당 값 만큼 readCount 증가 or 1++
     * @throws Exception
     */


    @Override
    @Transactional
    @Async
    public void upReadCount(int itemSeq, int userAge) throws Exception {

        checkIsItemByItemSeq(itemSeq);

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
    public void upReadCount(int itemSeq, int userAge, int readValue) throws Exception {
        log.info(this.getClass().getName() + "upCountCustom Start!");

        checkIsItemByItemSeq(itemSeq);

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

    private ItemEntity checkIsItemByItemSeq(final int itemSeq) {

        return itemRepository.findById(itemSeq).orElseThrow(() -> new ItemException(ItemExceptionResult.ITEM_NOT_FOUND));

    }
}
