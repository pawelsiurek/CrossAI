#pragma once
#include <vector>
#include <memory>
#include "Item.h"

class Recommender {
public:
    virtual ~Recommender() = default;
    virtual std::vector<Item> recommend(
        const std::vector<std::string>& preferredGenres,
        const std::vector<Item>& items
    ) = 0;
};