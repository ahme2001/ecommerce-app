import { getCategories } from "../../api/category-api";
import { useEffect, useState } from "react";

export default function ProductFilters({ filters, setFilters }) {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      const data = await getCategories();
      setCategories(data.content || []);
    };

    fetchCategories();
  }, []);

  return (
    <div className="bg-white p-4 shadow rounded space-y-4">

      {/* Search */}
      <input
        type="text"
        placeholder="Search in description..."
        className="w-full border p-2 rounded"
        value={filters.search}
        onChange={(e) =>
          setFilters({ ...filters, search: e.target.value, page: 0 })
        }
      />

      {/* Category */}
      <select
        className="w-full border p-2 rounded"
        value={filters.category}
        onChange={(e) =>
          setFilters({ ...filters, category: e.target.value, page: 0 })
        }
      >
        <option value="">All Categories</option>
        {categories.map((category) => (
          <option key={category.categoryId} value={category.categoryId}>
            {category.categoryName}
          </option>
        ))}
      </select>

      {/* Price Range */}
      <input
        type="number"
        placeholder="Max Price"
        className="w-full border p-2 rounded"
        value={filters.maxPrice}
        onChange={(e) =>
          setFilters({ ...filters, maxPrice: e.target.value, page: 0 })
        }
      />

    </div>
  );
}