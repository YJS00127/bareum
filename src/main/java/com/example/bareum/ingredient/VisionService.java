package com.example.bareum.ingredient;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class VisionService {

    @Value("${google.cloud.vision.credentials.path}")
    private String credentialsPath;

    public String extractText(MultipartFile image) {
        try {
            ByteString imageBytes = ByteString.copyFrom(image.getBytes());

            Image visionImage = Image.newBuilder()
                    .setContent(imageBytes)
                    .build();

            Feature feature = Feature.newBuilder()
                    .setType(Feature.Type.DOCUMENT_TEXT_DETECTION)
                    .build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .setImage(visionImage)
                    .addFeatures(feature)
                    .build();

            try (ImageAnnotatorClient client = createVisionClient()) {
                BatchAnnotateImagesResponse response =
                        client.batchAnnotateImages(List.of(request));

                AnnotateImageResponse result = response.getResponses(0);

                if (result.hasError()) {
                    throw new RuntimeException("Vision API 오류: "
                            + result.getError().getMessage());
                }

                if(!result.hasFullTextAnnotation()){
                    return "";
                }

                return result.getFullTextAnnotation().getText();
            }

        } catch (IOException e) {
            throw new RuntimeException("이미지 텍스트 추출 실패", e);
        }
    }

    private ImageAnnotatorClient createVisionClient() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(credentialsPath));

        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        return ImageAnnotatorClient.create(settings);
    }
}