package com.company.bezkoderonetoone.controller;


import com.company.bezkoderonetoone.exception.ResourceNotFoundException;
import com.company.bezkoderonetoone.model.Comment;
import com.company.bezkoderonetoone.model.Tutorial;
import com.company.bezkoderonetoone.repository.CommentRepository;
import com.company.bezkoderonetoone.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TutorialRepository tutorialRepository;

    @GetMapping({"/tutorials/{tutorialId}/comments"})
    public ResponseEntity<List<Comment>> getAllCommentsByTutorialId(@PathVariable(value = "tutorialId") Long tutorialId) {
        if(!tutorialRepository.existsById(tutorialId)){
            throw new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId);
        }
        List<Comment> comments = commentRepository.findByTutorialId(tutorialId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(
            @PathVariable(value = "id")Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Not found comment with id = " + id));
        return  new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable(value = "tutorialId") Long tutorialId,
                                                 @RequestBody Comment commentRequest) {
        Tutorial tutorial = tutorialRepository.findById(tutorialId)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));
        commentRequest.setTutorial(tutorial);
        Comment comment = commentRepository.save(commentRequest);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") Long id,
                                                 @RequestBody Comment commentRequest){
        System.out.println(commentRequest);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CommentId " + id + " not found"));
        System.out.println(comment);
        comment.setContent(commentRequest.getContent());
        Comment newComment = commentRepository.save(comment);
        return new ResponseEntity<>(newComment, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") long id){
        commentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<Comment> deleteAllCommentsOfTutorial(@PathVariable(value = "tutorialId") Long tutorialId){
        if(!tutorialRepository.existsById(tutorialId)){
            throw new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId);
        }
        commentRepository.deleteByTutorialId(tutorialId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
