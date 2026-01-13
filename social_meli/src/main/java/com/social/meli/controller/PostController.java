package com.social.meli.controller;

import com.social.meli.controller.docs.PostControllerDocs;
import com.social.meli.dto.post.PostCreateDTO;
import com.social.meli.dto.post.PostResponseDTO;
import com.social.meli.dto.post.PostUpdateDTO;
import com.social.meli.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController implements PostControllerDocs {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> PostCreate(@RequestBody @Valid PostCreateDTO postCreateDTO) {
        PostResponseDTO postResponseDTO = postService.postCreate(postCreateDTO);
        return ResponseEntity.status(201).body(postResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.findPostById(id));
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<PostResponseDTO>> findAllByProfile(@PathVariable Long profileId) {
        return ResponseEntity.ok(postService.findAllByProfile(profileId));
    }

    @GetMapping("profile/{profileId}/promo")
    public ResponseEntity<List<PostResponseDTO>> findAllByPromo(@PathVariable Long profileId) {
        return ResponseEntity.ok(postService.findAllByIsPromo(profileId));
    }

    @GetMapping("/timeline/{buyerProfileId}")
    public ResponseEntity<List<PostResponseDTO>> findAllByTimeLine(@PathVariable Long buyerProfileId) {
        return ResponseEntity.ok(postService.timeline(buyerProfileId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable UUID id, @RequestBody PostUpdateDTO postUpdateDTO) {
        PostResponseDTO update = postService.updatePost(id, postUpdateDTO);
        return ResponseEntity.ok(update);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/inactive/{id}")
    public ResponseEntity<Void> inactive(@PathVariable UUID id) {
        postService.inactivatePost(id);
        return ResponseEntity.noContent().build();
    }
}

