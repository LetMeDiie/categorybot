package kz.amihady.categorytree.service;


import kz.amihady.categorytree.service.response.CategoryTreeExelResponse;
import kz.amihady.categorytree.service.response.CategoryTreeStringResponse;

public interface CategoryTreeService {

    CategoryTreeStringResponse getTreeAsString(Long userId);
    CategoryTreeExelResponse getTreeAsExel(Long userId);
}
