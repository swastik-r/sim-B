package com.inventory.purchaseorder.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.inventory.purchaseorder.dto.ReturnToVendorCombinedDto;
import com.inventory.purchaseorder.dto.ReturnToVendorInfodto;
import com.inventory.purchaseorder.dto.ReturnToVendorProcessDto;
import com.inventory.purchaseorder.dto.ReturnToVendorProductsdto;

import com.inventory.purchaseorder.entity.ReturnToVendorInfo;
import com.inventory.purchaseorder.entity.ReturnToVendorProcessInfo;
import com.inventory.purchaseorder.entity.ReturnToVendorProcessProducts;
import com.inventory.purchaseorder.entity.ReturnToVendorProducts;

import com.inventory.purchaseorder.repository.ReturnTovendorInfoRepo;
import com.inventory.purchaseorder.repository.ReturnTovendorProcessInfoRepo;
import com.inventory.purchaseorder.repository.ReturnTovendorProcessProductsRepo;
import com.inventory.purchaseorder.service.ReturnToVendorService;
import com.inventory.purchaseorder.repository.ReturnTovendorProductsRepo;

@Service
public class RerurnToVendorServiceImpl implements ReturnToVendorService {

	@Autowired
	private ReturnTovendorInfoRepo rtvInfoRepo;

	@Autowired
	private ReturnTovendorProductsRepo rtvProductsRepo;

	@Autowired
	private ReturnTovendorProcessInfoRepo rtvProcessInfoRepo;

	@Autowired
	private ReturnTovendorProcessProductsRepo rtvProcessProductsRepo;

	// Function to save RTV list
	@Override
	public String saveProducts(ReturnToVendorCombinedDto RTVCombinedDto) {

		ReturnToVendorInfo RTVInfo = new ReturnToVendorInfo(RTVCombinedDto.getRtvInfodto().getPoNumber(),
				RTVCombinedDto.getRtvInfodto().getSupplierId(), RTVCombinedDto.getRtvInfodto().getSupplierName(),
				RTVCombinedDto.getRtvInfodto().getDate(), RTVCombinedDto.getRtvInfodto().getStatus());

		rtvInfoRepo.save(RTVInfo);

		ReturnToVendorInfo RTVInfo1 = rtvInfoRepo.findByrtvId(RTVInfo.getRtvId());
		List<ReturnToVendorProducts> rtvProducts = new ArrayList<>();
		for (int i = 0; i < RTVCombinedDto.getRtvProductsdto().size(); i++) {
			rtvProducts.add(new ReturnToVendorProducts(RTVCombinedDto.getRtvProductsdto().get(i).getItemNumber(),
					RTVCombinedDto.getRtvProductsdto().get(i).getItemName(),
					RTVCombinedDto.getRtvProductsdto().get(i).getCategory(),
					RTVCombinedDto.getRtvProductsdto().get(i).getColor(),
					RTVCombinedDto.getRtvProductsdto().get(i).getPrice(),
					RTVCombinedDto.getRtvProductsdto().get(i).getSize(),
					RTVCombinedDto.getRtvProductsdto().get(i).getImageData(),
					RTVCombinedDto.getRtvProductsdto().get(i).getStore(),
					RTVCombinedDto.getRtvProductsdto().get(i).getReturnQty(), RTVInfo1));
		}

		rtvProductsRepo.saveAll(rtvProducts);
		return "Products saved successfully";
	}

	// Function to get RTV list
	@Override
	public ReturnToVendorCombinedDto getRTVProducts(int rtvId) {
		ReturnToVendorCombinedDto RTvCombinedDto = new ReturnToVendorCombinedDto();
		ReturnToVendorInfo RTVInfo = rtvInfoRepo.findByrtvId(rtvId);

		if (!RTVInfo.getStatus().equals("complete")) {
			ReturnToVendorInfodto ReturnToVendorInfodto = new ReturnToVendorInfodto(RTVInfo.getPoNumber(),
					RTVInfo.getSupplierId(), RTVInfo.getSupplierName(), RTVInfo.getDate(), RTVInfo.getStatus());

			RTvCombinedDto.setRtvInfodto(ReturnToVendorInfodto);

			List<ReturnToVendorProducts> rtvProducts = rtvProductsRepo.findByrtvInfo(RTVInfo);
			List<ReturnToVendorProductsdto> rtvProductsdto = new ArrayList<>();

			for (int i = 0; i < rtvProducts.size(); i++) {
				rtvProductsdto.add(new ReturnToVendorProductsdto(rtvProducts.get(i).getId(),
						rtvProducts.get(i).getItemNumber(), rtvProducts.get(i).getItemName(),
						rtvProducts.get(i).getCategory(), rtvProducts.get(i).getColor(), rtvProducts.get(i).getPrice(),
						rtvProducts.get(i).getSize(), rtvProducts.get(i).getImageData(), rtvProducts.get(i).getStore(),
						rtvProducts.get(i).getReturnQty(), rtvProducts.get(i).getRtvInfo().getRtvId()));
			}

			RTvCombinedDto.setRtvProductsdto(rtvProductsdto);
			System.out.println("RTvCombinedDto : " + RTvCombinedDto);
		}

		return RTvCombinedDto;

	}

	// Function to save RTV data in RTV master table
	@Override
	public String saveRTVProcessProducts(ReturnToVendorProcessDto RTVProcessDto, int rtvId) {
		ReturnToVendorInfo RTVInfo = rtvInfoRepo.findByrtvId(rtvId);
		ReturnToVendorProcessInfo RTVProcessInfo = new ReturnToVendorProcessInfo(
				RTVProcessDto.getRtvProcessInfo().getPoNumber(), RTVProcessDto.getRtvProcessInfo().getSupplierId(),
				RTVProcessDto.getRtvProcessInfo().getSupplierName(), RTVProcessDto.getRtvProcessInfo().getStatus(),
				RTVProcessDto.getRtvProcessInfo().getReason(), RTVProcessDto.getRtvProcessInfo().getDate());

		rtvProcessInfoRepo.save(RTVProcessInfo);

		ReturnToVendorProcessInfo RTVProcessInfo1 = rtvProcessInfoRepo.findByrtvId(RTVProcessInfo.getRtvId());
		List<ReturnToVendorProcessProducts> rtvProcessProducts = new ArrayList<>();
		for (int i = 0; i < RTVProcessDto.getRtvProcessProducts().size(); i++) {
			rtvProcessProducts
					.add(new ReturnToVendorProcessProducts(RTVProcessDto.getRtvProcessProducts().get(i).getItemNumber(),
							RTVProcessDto.getRtvProcessProducts().get(i).getItemName(),
							RTVProcessDto.getRtvProcessProducts().get(i).getCategory(),
							RTVProcessDto.getRtvProcessProducts().get(i).getColor(),
							RTVProcessDto.getRtvProcessProducts().get(i).getPrice(),
							RTVProcessDto.getRtvProcessProducts().get(i).getSize(),
							RTVProcessDto.getRtvProcessProducts().get(i).getImageData(),
							RTVProcessDto.getRtvProcessProducts().get(i).getStore(),
							RTVProcessDto.getRtvProcessProducts().get(i).getReturnQty(), RTVProcessInfo1));
		}

		rtvProcessProductsRepo.saveAll(rtvProcessProducts);
		RTVInfo.setStatus("complete"); // setting the status of rtv list..
		rtvInfoRepo.save(RTVInfo);
		return "Products saved successfully";
	}

	// Function to get RTV list from Master process table
	@Override
	public List<ReturnToVendorProcessProducts> getRTVProcessProducts(int rtvId) {

		ReturnToVendorProcessInfo RTVinfo = rtvProcessInfoRepo.findByrtvId(rtvId);
		List<ReturnToVendorProcessProducts> products = rtvProcessProductsRepo.findByrtvProcessInfo(RTVinfo);
		return products;

	}

	// Function to get all RTV
	@Override
	public List<ReturnToVendorProcessInfo> getAllViewVendorReturn() {
		List<ReturnToVendorProcessInfo> RTVProcessInfo = rtvProcessInfoRepo.findAll();
		System.out.println("RTVProcessInfo : " + RTVProcessInfo);
		return RTVProcessInfo;
	}

	@Override
	public List<Integer> getAllRTVId() {
		List<ReturnToVendorInfo> ReturnToVendor = rtvInfoRepo.findAll();
		List<Integer> RTVIdList = new ArrayList<>();
		for (int i = 0; i < ReturnToVendor.size(); i++) {
			if (ReturnToVendor.get(i).getStatus().equals("pending")) {
				RTVIdList.add(ReturnToVendor.get(i).getRtvId());
			}
		}
		return RTVIdList;
	}

}
