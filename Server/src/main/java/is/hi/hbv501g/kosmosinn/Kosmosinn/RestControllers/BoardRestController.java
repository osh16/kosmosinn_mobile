package is.hi.hbv501g.kosmosinn.Kosmosinn.RestControllers;

import is.hi.hbv501g.kosmosinn.Kosmosinn.Entities.Board;
import is.hi.hbv501g.kosmosinn.Kosmosinn.Entities.Topic;
import is.hi.hbv501g.kosmosinn.Kosmosinn.Entities.User;
import is.hi.hbv501g.kosmosinn.Kosmosinn.Services.BoardService;
import is.hi.hbv501g.kosmosinn.Kosmosinn.Services.CommentService;
import is.hi.hbv501g.kosmosinn.Kosmosinn.Services.TopicService;
import is.hi.hbv501g.kosmosinn.Kosmosinn.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/boards")
@RestController
public class BoardRestController {
    private UserService userService;
    private TopicService topicService;
    private BoardService boardService;
    private CommentService commentService;

    @Autowired
    public BoardRestController(UserService userService, TopicService topicService, BoardService boardService, CommentService commentService) {
        this.userService = userService;
        this.topicService = topicService;
        this.boardService = boardService;
        this.commentService = commentService;
    }

    @Autowired
    HttpSession session;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Board> getAllBoards() {
        return boardService.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Board getBoardById(@PathVariable("id") long id) {
        return boardService.findById(id).get();
    }

    @RequestMapping(value = "/{id}/topics", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Topic> getTopicsById(@PathVariable("id") long id) {
        return topicService.findAllByBoardId(id);
    }

   @PostMapping(value = "/addBoard", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Board addBoard(@RequestBody Board board) {
        boardService.save(board);
        return board;
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Board editBoard(@Valid @PathVariable("id") long id, @RequestBody Board editedBoard) {
        Board board = boardService.findById(id).get();
        
        if (!editedBoard.getName().isBlank()) {
            board.setName(editedBoard.getName());
        }
        if (!editedBoard.getDescription().isBlank()) {
            board.setDescription(editedBoard.getDescription());
        }
        
        boardService.save(board);
        
        return board;
    }

    @DeleteMapping("/{id}/delete")
    public void deleteBoard(@PathVariable("id") long id) {
        Board board = boardService.findById(id).get();
        boardService.delete(board);
    }

    @PostMapping(value = "/{id}/addTopic", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Topic addTopic(@PathVariable("id") long id, @RequestBody Topic topic) {
        Board board = boardService.findById(id).get();
        topic.setBoard(board);
        topicService.save(topic);
        return topic;
    }

}
