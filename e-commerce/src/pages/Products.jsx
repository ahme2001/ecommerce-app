import { useEffect, useState } from "react";
import { getProducts, getProductsByCategory, getProductsByKeyword } from "../api/product-api";

import ProductGrid from "../components/product/ProductGrid";
import ProductFilters from "../components/product/ProductFilters";
import Pagination from "../components/common/Pagination";

export default function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);

  const [filters, setFilters] = useState({
    search: "",
    category: "",
    maxPrice: "",
    page: 0,
    size: 10,
  });

  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    let isActive = true;

    const fetchProducts = async () => {
      try {
        setLoading(true);

        const queryParams = {
          pageNumber: filters.page,
          pageSize: filters.size,
          sortBy: filters.category ? "productId" : filters.search ? "productName" : "price",
          sortOrder: "asc",
        };

        let data;

        if (filters.category) {
          data = await getProductsByCategory(filters.category, queryParams);
        } else if (filters.search?.trim()) {
          data = await getProductsByKeyword(filters.search.trim(), queryParams);
        } else {
          data = await getProducts(queryParams);
        }

        if (isActive) {
          setProducts(data?.content || []);
          setTotalPages(data?.totalPages || 0);
        }
      } catch (err) {
        console.error("Error fetching products:", err);
        if (isActive) {
          setProducts([]);
          setTotalPages(0);
        }
      } finally {
        if (isActive) {
          setLoading(false);
        }
      }
    };

    void fetchProducts();

    return () => {
      isActive = false;
    };
  }, [filters.category, filters.page, filters.search, filters.size]);

  return (
    <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">

      {/* Filters */}
      <div className="lg:col-span-1">
        <ProductFilters filters={filters} setFilters={setFilters} />
      </div>

      {/* Products */}
      <div className="lg:col-span-3">

        {loading ? (
          <p>Loading...</p>
        ) : (
          <>
            <ProductGrid products={products} />

            <Pagination
              page={filters.page}
              totalPages={totalPages}
              setPage={(newPage) =>
                setFilters((prev) => ({ ...prev, page: newPage }))
              }
            />
          </>
        )}

      </div>
    </div>
  );
}