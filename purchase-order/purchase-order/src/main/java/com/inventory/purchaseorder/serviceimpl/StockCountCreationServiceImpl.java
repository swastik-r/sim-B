
package com.inventory.purchaseorder.serviceimpl;

import java.time.LocalDate;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.purchaseorder.service.StockCountCreationService;
import com.inventory.purchaseorder.dto.StockCountCreationCombinedDto;
import com.inventory.purchaseorder.dto.StockCountCreationProductsdto;
import com.inventory.purchaseorder.dto.StockCountCreationdto;
import com.inventory.purchaseorder.entity.Category;
import com.inventory.purchaseorder.entity.StockCountCreation;
import com.inventory.purchaseorder.entity.StockCountCreationProducts;
import com.inventory.purchaseorder.repository.CategoryRepo;
import com.inventory.purchaseorder.repository.StockCreationProductsRepo;
import com.inventory.purchaseorder.repository.StockCreationRepo;

@Service
public class StockCountCreationServiceImpl implements StockCountCreationService {

	@Autowired
	private StockCreationRepo creationRepo;

	@Autowired
	private StockCreationProductsRepo creationProductsRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public StockCountCreationCombinedDto saveProducts(StockCountCreationCombinedDto StockCountCreationCombinedDto) {

		StockCountCreation ScCreation = creationRepo
				.findByCountId(StockCountCreationCombinedDto.getCreationdto().getCountId());
		if (ScCreation == null) {
			StockCountCreation stockCountCreation = new StockCountCreation(
					StockCountCreationCombinedDto.getCreationdto().getCountId(),
					StockCountCreationCombinedDto.getCreationdto().getCountDescription(),
					StockCountCreationCombinedDto.getCreationdto().getDate(),
					StockCountCreationCombinedDto.getCreationdto().getStatus(),
					StockCountCreationCombinedDto.getCreationdto().getTotalBookQty(),
					StockCountCreationCombinedDto.getCreationdto().getReCount(),
					StockCountCreationCombinedDto.getCreationdto().getStore());

			creationRepo.save(stockCountCreation);

			StockCountCreation ScCreation1 = creationRepo
					.findByCountId(StockCountCreationCombinedDto.getCreationdto().getCountId());

			for (int i = 0; i < StockCountCreationCombinedDto.getCreationProductsdto().size(); i++) {
				StockCountCreationProducts stockCountCreationProducts = new StockCountCreationProducts(
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getItemNumber(),
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getItemName(),
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getCategory(),
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getColor(),
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getPrice(),
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getSize(),
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getImageData(),
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getStore(),
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getBookQty(),
						StockCountCreationCombinedDto.getCreationProductsdto().get(i).getSku(), ScCreation1);
				creationProductsRepo.save(stockCountCreationProducts);

			}
		}

		return StockCountCreationCombinedDto;

	}

	@Override
	public StockCountCreationCombinedDto getProductsByDate(LocalDate date, String storeName) {
		StockCountCreation ScCreation = creationRepo.findByDateAndStore(date, storeName);
		System.out.println("local date : " + date);
		StockCountCreationCombinedDto stockCountCreationCombinedDto = new StockCountCreationCombinedDto();
		List<StockCountCreationProductsdto> stockCountCreationProductsdto = new ArrayList<>();
		if (ScCreation.getStatus().equals("pending")) {

			List<StockCountCreationProducts> stockCountCreationProducts = creationProductsRepo
					.findByStockcount(ScCreation);

			StockCountCreationdto stockCountCreationdto = new StockCountCreationdto(ScCreation.getCountId(),
					ScCreation.getCountDescription(), ScCreation.getDate(), ScCreation.getStatus(),
					ScCreation.getTotalBookQty(), ScCreation.getReCount(), ScCreation.getStore());

			stockCountCreationCombinedDto.setCreationdto(stockCountCreationdto);

			for (int i = 0; i < stockCountCreationProducts.size(); i++) {
				Category category = categoryRepo.findByCategory(stockCountCreationProducts.get(i).getCategory());
				String categoryLocation = category.getLocation();
				stockCountCreationProductsdto.add(new StockCountCreationProductsdto(
						stockCountCreationProducts.get(i).getId(), stockCountCreationProducts.get(i).getItemNumber(),
						stockCountCreationProducts.get(i).getItemName(),
						stockCountCreationProducts.get(i).getCategory(), stockCountCreationProducts.get(i).getColor(),
						stockCountCreationProducts.get(i).getPrice(), stockCountCreationProducts.get(i).getSize(),
						stockCountCreationProducts.get(i).getImageData(), stockCountCreationProducts.get(i).getStore(),
						stockCountCreationProducts.get(i).getBookQty(),
						stockCountCreationProducts.get(i).getStockcount().getCountId(),
						stockCountCreationProducts.get(i).getSku(), categoryLocation));
			}

			stockCountCreationCombinedDto.setCreationProductsdto(stockCountCreationProductsdto);
			System.out.println("stockCountCreationProducts : " + stockCountCreationProducts);
		}
		return stockCountCreationCombinedDto;
	}

	@Override
	public List<StockCountCreationProducts> getStockCountProductsByCountId(int id) {
		// System.out.print("id "+id);
		StockCountCreation countObject = creationRepo.findByCountId(id);
		List<StockCountCreationProducts> stockCountProducts = creationProductsRepo.findByStockcount(countObject);
		return stockCountProducts;

	}

}
