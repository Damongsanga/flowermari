import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Bouquet } from "../../types/BouquetList";
import { useBouquetList } from "../../api/useBouquetList";

export const BouquetListTestPage = () => {
    const [type, setType] = useState('');
    const [searchKeyword, setSearchKeyword] = useState('');
    const [orderBy, setOrderBy] = useState('');
    const [searchParams, setSearchParams] = useState({
        type: '',
        searchKeyword: '',
        orderBy: ''
    });
    const { bouquets, loading, error, hasMore,fetchMoreData,fetchData  } = useBouquetList(searchParams.type, searchParams.searchKeyword, searchParams.orderBy);

    const observer = useRef<IntersectionObserver | null>(null);
    const lastBouquetElementRef = useCallback((node: Element | null) => {
        if (loading || !hasMore) return;
        if (observer.current) observer.current.disconnect();
        observer.current = new IntersectionObserver(entries => {
            if (entries[0].isIntersecting && hasMore && !loading) {
                fetchMoreData ();
            }
        });
        if (node) observer.current.observe(node);
    }, [loading, hasMore]);
    const handleSearch = () => {
        console.log("검색");
        setSearchParams({
            type,
            searchKeyword,
            orderBy
        });
        fetchData();
    };
    return (
        <div>
            <select value={type} onChange={e => setType(e.target.value)}>
                <option value="">Select Type</option>
                <option value="NAME">Name</option>
                <option value="MEANING">Meaning</option>
                <option value="TEXT">Text</option>
            </select>
            <input
                type="text"
                value={searchKeyword}
                onChange={e => setSearchKeyword(e.target.value)}
                placeholder="Search keyword"
            />
            <select value={orderBy} onChange={e => setOrderBy(e.target.value)}>
                <option value="">Order By</option>
                <option value="RECENT">Recent</option>
                <option value="LIKE">Like</option>
            </select>
            <button onClick={handleSearch}>검색</button>
            <div>
                {bouquets.map((bouquet, index) => (
                    <div key={bouquet.bouquetId} ref={index === bouquets.length - 1 ? lastBouquetElementRef : null}>
                        <h3>{bouquet.whom} - {bouquet.situation}</h3>
                        <p>{bouquet.message}</p>
                        <img src={bouquet.imageUrl} alt=""/>
                    </div>
                ))}
                {loading && <p>Loading more bouquets...</p>}
                {error && <p>Error occurred: {error.message}</p>}
            </div>
        </div>
    );
};
