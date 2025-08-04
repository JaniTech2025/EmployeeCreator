import React, { useEffect, useState } from "react";
import { Select, Spinner, Text } from "@chakra-ui/react";

export interface ImagePickerProps {
  onSelect: (url: string) => void;
}

export const ImagePicker: React.FC<ImagePickerProps> = ({ onSelect }) => {
  const [images, setImages] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8080/api/cloudinary/images")
      .then((res) => res.json())
      .then((data: string[]) => {
        setImages(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Failed to fetch Cloudinary images", err);
        setLoading(false);
      });
  }, []);

  if (loading) return <Spinner size="md" />;
  if (images.length === 0) return <Text>No image URLs found.</Text>;

  return (
    <Select
      placeholder="Select an image URL"
      onChange={(e) => onSelect(e.target.value)}
      size="sm"
    >
      {images.map((url) => (
        <option key={url} value={url} >
          {url}
        </option>
      ))}
    </Select>
  );
};