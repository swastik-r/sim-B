package com.inventory.purchaseorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inventory.purchaseorder.dto.ReturnToVendorCombinedDto;
import com.inventory.purchaseorder.dto.ReturnToVendorProcessDto;
import com.inventory.purchaseorder.entity.InventoryAdjustment;
import com.inventory.purchaseorder.entity.ReturnToVendorInfo;
import com.inventory.purchaseorder.entity.ReturnToVendorProcessInfo;
import com.inventory.purchaseorder.entity.ReturnToVendorProcessProducts;
import com.inventory.purchaseorder.service.ReturnToVendorService;

@RestController
@RequestMapping("/returntovendor")
public class ReturnToVendorController {

	@Autowired
	private ReturnToVendorService RTVService;

	// Api to save RTV list
	@PostMapping("/addrtv/itemlist")
	public ResponseEntity<String> add_Products(@RequestBody ReturnToVendorCombinedDto RTVCombinedDto) {
		String success_msg = RTVService.saveProducts(RTVCombinedDto);
		return new ResponseEntity<>(success_msg, HttpStatus.OK);
	}

	// Api to get RTV list
	@GetMapping("/getrtvitems/{rtvId}")
	public ResponseEntity<ReturnToVendorCombinedDto> getRTVProducts(@PathVariable int rtvId) {
		ReturnToVendorCombinedDto RTVCombinedDto = RTVService.getRTVProducts(rtvId);
		return new ResponseEntity<>(RTVCombinedDto, HttpStatus.OK);
	}

	// Api to create and save RTV data in RTV table
	@PostMapping("/addrtvitems/{rtvId}")
	public ResponseEntity<String> addRTVProducts(@RequestBody ReturnToVendorProcessDto RTVProcessDto,
			@PathVariable int rtvId) {
		String success_msg = RTVService.saveRTVProcessProducts(RTVProcessDto,rtvId);
		return new ResponseEntity<>(success_msg, HttpStatus.OK);
	}

	// Api to get RTV list
	@GetMapping("/getallrtv")
	public ResponseEntity<List<ReturnToVendorProcessInfo>> getAllRTV() {
		List<ReturnToVendorProcessInfo> ReturnToVendorProcessInfo = RTVService.getAllViewVendorReturn();
		return new ResponseEntity<>(ReturnToVendorProcessInfo, HttpStatus.OK);
	}

	// Api to get RTV master process list by id
	@GetMapping("/getrtv/{rtvId}")
	public ResponseEntity<List<ReturnToVendorProcessProducts>> getRTV(@PathVariable int rtvId) {
		List<ReturnToVendorProcessProducts> products = RTVService.getRTVProcessProducts(rtvId);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping("/all/rtvid")
	public ResponseEntity<List<Integer>> getMatchedRTVListById() {
		List<Integer> rtv_list = RTVService.getAllRTVId();
		return new ResponseEntity<>(rtv_list, HttpStatus.OK);
	}

	
}
