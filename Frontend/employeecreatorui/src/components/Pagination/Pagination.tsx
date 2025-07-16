import React from "react";
import { Button, HStack, Text } from "@chakra-ui/react";


type PaginationProps = {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
};

const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
}) => {
  if (totalPages <= 1) return null;

  const handleClick = (page: number) => {
    if (page !== currentPage && page >= 1 && page <= totalPages) {
      onPageChange(page);
    }
  };

  const getPageNumbers = (): (number | string)[] => {
    const pages: (number | string)[] = [];
    const maxDisplayed = 5;

    if (totalPages <= maxDisplayed) {
      for (let i = 1; i <= totalPages; i++) pages.push(i);
    } else {
      const start = Math.max(2, currentPage - 1);
      const end = Math.min(totalPages - 1, currentPage + 1);

      pages.push(1);
      if (start > 2) pages.push("...");

      for (let i = start; i <= end; i++) pages.push(i);

      if (end < totalPages - 1) pages.push("...");
      pages.push(totalPages);
    }

    return pages;
  };

  return (
    <HStack spacing={2} justify="center" mt={4}>
      <Button
        onClick={() => handleClick(currentPage - 1)}
        isDisabled={currentPage === 1}
        size="sm"
      >
        {"<<"}
      </Button>

      {getPageNumbers().map((item, index) =>
        typeof item === "number" ? (
          <Button
            key={index}
            onClick={() => handleClick(item)}
            size="sm"
            colorScheme={item === currentPage ? "blue" : "gray"}
            variant={item === currentPage ? "solid" : "outline"}
          >
            {item}
          </Button>
        ) : (
          <Text key={index} px={2} userSelect="none">
            {item}
          </Text>
        )
      )}

      <Button
        onClick={() => handleClick(currentPage + 1)}
        isDisabled={currentPage === totalPages}
        size="sm"
      >
        {">>"}
      </Button>
    </HStack>
  );
};

export default Pagination;
