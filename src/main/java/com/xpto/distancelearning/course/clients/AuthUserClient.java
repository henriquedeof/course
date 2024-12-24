package com.xpto.distancelearning.course.clients;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class AuthUserClient {

//======================================================================================================================
//    NOTE: Fields deleted as the communication is now asynchronous
//======================================================================================================================

//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private UtilsService utilsService;
//
//    @Value("${dl.api.url.authuser}")
//    private String REQUEST_URL_AUTHUSER;
//
//    public Page<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable){
//        List<UserDto> searchResult = null;
//        ResponseEntity<ResponsePageDto<UserDto>> result = null;
//        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUsersByCourse(courseId, pageable);
//        log.info("Request URL: {} ", url);
//        try{
//            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType = new ParameterizedTypeReference<>() { };
//            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
//            searchResult = result.getBody().getContent();
//            log.debug("Response Number of Elements: {} ", searchResult.size());
//        } catch (HttpStatusCodeException e){
//            log.error("Error request /courses {} ", e);
//        }
//        log.info("Ending request /users courseId {} ", courseId);
//        return result.getBody();
//    }
//
//    public ResponseEntity<UserDto> getOneUserById(UUID userId){
//        String url = REQUEST_URL_AUTHUSER + "/users/" + userId;
//        return restTemplate.exchange(url, HttpMethod.GET, null, UserDto.class);
//    }
//
//    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
//        String url = REQUEST_URL_AUTHUSER + "/users/" + userId + "/courses/subscription";
//        var courseUserDto = new CourseUserDto();
//        courseUserDto.setUserId(userId);
//        courseUserDto.setCourseId(courseId);
//        restTemplate.postForObject(url, courseUserDto, String.class);
//    }
//
//    public void deleteCourseInAuthUser(UUID courseId){
//        String url = REQUEST_URL_AUTHUSER + "/users/courses/" + courseId;
//        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
//    }
}