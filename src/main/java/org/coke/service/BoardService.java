package org.coke.service;

import org.coke.dto.BoardDTO;
import org.coke.dto.BoardImageDTO;
import org.coke.dto.PageRequestDTO;
import org.coke.dto.PageResultDTO;
import org.coke.entity.Board;
import org.coke.entity.BoardImage;
import org.coke.entity.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
    BoardDTO getBoard(Long bno);
    void removeWithRepliesAndImages(Long bno);
    void modify(BoardDTO boardDTO);


    default Map<String, Object> dtoToEntity(BoardDTO boardDTO){

        Map<String, Object> entityMap = new HashMap<>();

        Member member = Member.builder().email(boardDTO.getMemberEmail()).build();

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .member(member)
                .build();

        entityMap.put("board", board);

        List<BoardImageDTO> imageDTOList = boardDTO.getImageDTOList();

        if(imageDTOList != null && imageDTOList.size() > 0){

            List<BoardImage> boardImageList = imageDTOList.stream().map(boardImageDTO -> {

                BoardImage boardImage = BoardImage.builder()
                        .path(boardImageDTO.getFolderPath())
                        .uuid(boardImageDTO.getUuid())
                        .fileName(boardImageDTO.getFileName())
                        .board(board)
                        .build();

                return boardImage;

            }).collect(Collectors.toList());

            entityMap.put("imageList", boardImageList);
        }

        return entityMap;
    }

    default BoardDTO entityToDTO(Board board, List<BoardImage> boardImageList, Member member, Long replyCount){
        System.out.println("---------------------board service------------");
        System.out.println("board on service: " + board);
        System.out.println("boardImageList on service: " + boardImageList);
        System.out.println("member on service: " + member);
        System.out.println("replyCount on service: " + replyCount);

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .memberEmail(member.getEmail())
                .memberName(member.getName())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        if (boardImageList != null && boardImageList.size() > 0) {
            List<BoardImageDTO> boardImageDTOList = boardImageList.stream().map(boardImage -> {
                if (boardImage != null ){
                    return BoardImageDTO.builder()
                            .folderPath(boardImage.getPath())
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .build();
                }else {
                    return null;
                }
            }).collect(Collectors.toList());
            boardDTO.setImageDTOList(boardImageDTOList);
        }

        boardDTO.setReplyCount(replyCount.intValue());

        return boardDTO;
    }

}
