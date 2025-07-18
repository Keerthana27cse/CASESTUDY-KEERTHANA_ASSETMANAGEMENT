import { useState } from "react";
import { useEffect } from "react";




function useAssetDetails(assetId, token) {
  const [assetName, setAssetName] = useState('');
  const [categoryName, setCategoryName] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAssetDetails = async () => {
      if (!assetId || !token) return;

      try {
        const res = await fetch(`http://localhost:8081/api/assets/${assetId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (res.ok) {
          const data = await res.json();
          setAssetName(data.assetName || '');
          setCategoryName(data.categoryName || '');
          setCategoryId(data.categoryId || ''); // 
        } else {
          console.error('Failed to fetch asset details');
        }
      } catch (err) {
        console.error('Error fetching asset:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchAssetDetails();
  }, [assetId, token]);

  return { assetName, categoryName, categoryId, loading };
}

export default useAssetDetails;