package api.crabteam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.crabteam.controllers.requestsBody.NewCodelist;
import api.crabteam.model.entities.builders.CodelistBuilder;
import api.crabteam.model.repositories.CodelistRepository;

import org.springframework.web.bind.annotation.RequestBody;

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
	
	@PostMapping("/upload")
    @ApiOperation("Creates a new codelist by uploading it.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Codelist successfully created."),
        @ApiResponse(code = 404, message = "Codelist wasn't created.")
    })
	public void uploadCodelist (@RequestBody NewCodelist newCodelist) {
		codelistBuilder.setCodelistRepository(codelistRepository);
		
		
	}

}
