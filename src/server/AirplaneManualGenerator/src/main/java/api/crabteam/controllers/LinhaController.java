package api.crabteam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.crabteam.controllers.requestsBody.NewLine;
import api.crabteam.model.repositories.CodelistRepository;
import api.crabteam.model.repositories.LinhaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Linha controller
 * @author Bárbara Port
 *
 */
@RestController
@RequestMapping("/linha")
@Api("Line API")
public class LinhaController {
	
	@Autowired
	CodelistRepository codelistRepository;
	
	@Autowired
	LinhaRepository linhaRepository;
	
	@PostMapping("/new/{codelistName}")
	@ApiOperation("Creates a new line into a codelist.")
	@ApiResponses({
        @ApiResponse(code = 200, message = "Line successfully created."),
        @ApiResponse(code = 400, message = "Line wasn't created.")
    })
	public void createLine (@RequestBody NewLine newLine, @PathVariable String codelistName) {
		
	}

}
