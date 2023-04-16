package com.room.reservation.service;

/**
 * Description :
 *
 * dtoToEntity(BoardDTO) : DTO가 연관관계를 가진 Board엔티티객체와
 * Member엔티티객체를 구성해야하므로 내부적으로 Member엔티티를 처리하는 과정을 거칩니다.
 *
 *
 */
public interface BoardService {
    void initBoardDataBase();
    Long register(BoardDTO dto);

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); //목록처리

    BoardDTO get(Long bno);

    void removeWithReplies(Long bno);

    void modify(BoardDTO boardDTO);

    default Board dtoToEntity(BoardDTO dto){
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();

        return board;
    }

    /**
     * Description : 
     * Object[]을 DTO로 변환
     * PageResultDTO의 핵심은 JPQL의 결과로 나오는 Object[]을 DTO타입으로 변환하는 기능
     * 이 기능은 java.util.function을 이용해서 처리
     * 현재 예제의 경우 JPQL의 실행결과로 나오는 Object[]을 BoardDTO로 처리해주어야합니다.
     * 
     * Object[]의 내용은 Board와 Member, 댓글의 수는 Long 타입으로 나오게 되므로 
     * 이를 파라미터로 전달받아 BoardDTO를 구성하도록합니다.
     * 
     * @return
     */
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //long으로 나오므로 int로 처리
                .build();
        return boardDTO;
    }
}
