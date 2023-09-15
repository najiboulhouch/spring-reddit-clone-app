package net.najiboulhouch.redditcloneapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.najiboulhouch.redditcloneapp.dto.SubredditDto;
import net.najiboulhouch.redditcloneapp.exceptions.ResourceNotFoundException;
import net.najiboulhouch.redditcloneapp.mapper.SubredditMapper;
import net.najiboulhouch.redditcloneapp.model.Subreddit;
import net.najiboulhouch.redditcloneapp.repository.SubredditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    public SubredditDto save(SubredditDto subredditDto){
        Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    public List<SubredditDto> getAll() {
       return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
       return subredditMapper
               .mapSubredditToDto(subredditRepository
                       .findById(id)
                       .orElseThrow(() -> new ResourceNotFoundException("Subreddit not found with id : " + id )));
    }
}
