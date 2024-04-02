import { useState, useEffect, useCallback } from 'react';
import { Bouquet, BouquetSliceResponse } from "../types/BouquetList";
import { useLocalAxios } from "../utils/axios";

export const useBouquetList = (type: string, searchKeyword: string, orderBy: string) => {
    const [bouquets, setBouquets] = useState<Bouquet[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<Error | null>(null);
    const [hasMore, setHasMore] = useState(true);
    const [lastIndex, setLastIndex] = useState<number | null>(null);
    const [page, setPage] = useState(0);
    const axiosInstance = useLocalAxios();


    const fetchData = useCallback(async () => {
        console.log("----------");
        if (loading || !hasMore) return;
        console.log(`페치 전`, { page, lastIndex, type, searchKeyword, orderBy });
        setLoading(true);
        setError(null);

        try {
            const response = await axiosInstance.get<BouquetSliceResponse>('/bouquet/list', {
                params: {
                    type,
                    searchKeyword,
                    orderBy,
                    size: 4,
                    page,
                    ...(lastIndex !== null && { lastIndex })
                },
            });
            console.log(response.data);
            const newData = response.data.slice.content.filter(
                item => !bouquets.find(bouquet => bouquet.bouquetId === item.bouquetId)
            );
            console.log(`Filtered newData`, { filteredDataLength: newData.length, newData });
            setBouquets(prev => [...prev, ...newData]);
            setHasMore(!response.data.slice.last);
            setPage(prevPage => prevPage + 1);
            setLastIndex(response.data.lastIndex);
            console.log(`페치 후`, { newDataLength: response.data.slice.content.length, lastIndex: response.data.lastIndex, page , last: response.data.slice.last});
        } catch (error) {
            setError(new Error('An error occurred'));
        } finally {
            setLoading(false);
        }
    }, [loading, hasMore, type, searchKeyword, orderBy]);


    useEffect(() => {
        console.log("검색 useEffect");
        setPage(0);
        setLastIndex(null);
        setBouquets([]);
        setHasMore(true);
        // 검색 조건이 변경되었을 때만 fetchData 호출
        fetchData();
    }, []);
    // useEffect(() => {
    //     console.log("검색 useEffect");
    //     const initSearch = async () => {
    //         setPage(0);
    //         setLastIndex(null);
    //         setBouquets([]);
    //         setHasMore(true);
    //         await fetchData();
    //     };
    //
    //     initSearch();
    // }, [type, searchKeyword, orderBy]);

    const fetchMoreData = () => {
        if (loading || !hasMore) return;
        fetchData();
    };

    return { loading, error, bouquets, hasMore, fetchMoreData, fetchData };
};
