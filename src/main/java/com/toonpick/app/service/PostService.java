package com.toonpick.app.service;


import com.toonpick.app.dto.PostDTO;
import com.toonpick.app.entity.Post;
import com.toonpick.app.mapper.PostMapper;
import com.toonpick.app.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
        return PostMapper.toDTO(post);
    }

    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDTO createPost(PostDTO postDTO) {
        Post post = PostMapper.toEntity(postDTO);
        Post savedPost = postRepository.save(post);
        return PostMapper.toDTO(savedPost);
    }

    @Transactional
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());

        Post updatedPost = postRepository.save(existingPost);
        return PostMapper.toDTO(updatedPost);
    }

    @Transactional
    public boolean deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        } else {
            return false;
        }
    }
}
