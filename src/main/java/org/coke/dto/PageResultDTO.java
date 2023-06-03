package org.coke.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    private List<DTO> dtoList;

    private int size;

    private int page;

    private int totalPage;

    private int start, end;

    private Boolean prev, next;

    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageLists(result.getPageable());
    }

    private void makePageLists(Pageable pageable){

        this.page = pageable.getPageNumber() +1;
        this.size = pageable.getPageSize();

        int tempEnd = (int) ((Math.ceil(page/10.0)) * 10);

        start = tempEnd -9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

}
