package api.crabteam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.crabteam.controllers.requestsBody.NewCodelist;
import api.crabteam.model.entities.builders.CodelistBuilder;
import api.crabteam.model.repositories.CodelistRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Codelist controller
 * @author Bárbara Port
 *
 */
@RestController
@RequestMapping("/codelist")
@Api("Codelist API")
public class CodelistController {
	
	@Autowired
	CodelistRepository codelistRepository;
	
	@Autowired
	CodelistBuilder codelistBuilder;
	
	@PostMapping("/upload/{projectName}")
    @ApiOperation("Creates a new codelist by uploading it.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Codelist successfully created."),
        @ApiResponse(code = 400, message = "Codelist wasn't created.")
    })
	public ResponseEntity<?> uploadCodelist (@RequestBody NewCodelist newCodelist, @PathVariable String projectName) {
		codelistBuilder.setCodelistRepository(codelistRepository);
		codelistBuilder.build(newCodelist, projectName);
		
		if(codelistBuilder.isPersisted()) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		
		String failMessage = codelistBuilder.getFailMessage();
		
		return new ResponseEntity<String>(failMessage, HttpStatus.BAD_REQUEST);
	}

}
