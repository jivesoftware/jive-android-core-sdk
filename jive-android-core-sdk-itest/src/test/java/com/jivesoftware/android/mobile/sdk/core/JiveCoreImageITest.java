package com.jivesoftware.android.mobile.sdk.core;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.jivesoftware.android.mobile.sdk.entity.ContentBodyEntity;
import com.jivesoftware.android.mobile.sdk.entity.ContentEntity;
import com.jivesoftware.android.mobile.sdk.entity.ImageEntity;
import com.jivesoftware.android.mobile.sdk.entity.ImageListEntity;
import com.jivesoftware.android.mobile.sdk.entity.matcher.ListEntityMatchers;
import com.jivesoftware.android.mobile.sdk.entity.value.JiveCoreContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import static com.jivesoftware.android.mobile.sdk.entity.matcher.ImageEntityMatchers.imageContentType;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ImageEntityMatchers.imageHeight;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ImageEntityMatchers.imageName;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ImageEntityMatchers.imageRef;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ImageEntityMatchers.imageSize;
import static com.jivesoftware.android.mobile.sdk.entity.matcher.ImageEntityMatchers.imageWidth;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class JiveCoreImageITest extends AbstractITest {
    @SuppressWarnings({"unchecked", "RedundantTypeArguments"})
    @Test
    public void createStatusUpdateWithImagesThenFetchImages() throws Exception {
        String uuid = UUID.randomUUID().toString();

        ContentEntity newStatusUpdateEntity = new ContentEntity();
        newStatusUpdateEntity.type = JiveCoreContentType.update;
        newStatusUpdateEntity.subject = uuid;
        newStatusUpdateEntity.content = new ContentBodyEntity();
        newStatusUpdateEntity.content.text = "Hello world!";
        newStatusUpdateEntity.content.type = "text/html";
        newStatusUpdateEntity.visibility = "all";

        FileBody fileBody1 = new FileBody(new File("test-data/el-barto.jpg"), "image/jpeg");
        FileBody fileBody2 = new FileBody(new File("test-data/mr-burns.gif"), "image/gif");

        // ADMIN isn't allowed to create a status update.
        ContentEntity createdContentEntity = jiveCoreUser2.createContent(newStatusUpdateEntity, Arrays.asList(fileBody1, fileBody2)).call();

        ImageListEntity imageListEntity = jiveCoreUser2.fetchImages(createdContentEntity.resources.get("images").ref).call();

        // must be order independent because of JIVE-48402
        assertThat(imageListEntity, ListEntityMatchers.<ImageEntity, ImageListEntity>listEntities(
                Matchers.<ImageEntity>containsInAnyOrder(
                        allOf(
                                imageSize(greaterThan(0)),
                                imageContentType("image/jpeg"),
                                imageName("el-barto.jpg"),
                                imageRef(notNullValue(String.class)),
                                imageWidth(275),
                                imageHeight(342)),
                        allOf(
                                imageSize(greaterThan(0)),
                                imageContentType("image/gif"),
                                imageName("mr-burns.gif"),
                                imageRef(notNullValue(String.class)),
                                imageWidth(481),
                                imageHeight(592)))));
    }

    @Test
    public void uploadAndFetchImage() throws Exception {
        File sourceImageFile = new File("test-data/el-barto.jpg");
        ByteArrayOutputStream sourceImageFileByteArrayOutputStream = new ByteArrayOutputStream();
        Files.asByteSource(sourceImageFile).copyTo(sourceImageFileByteArrayOutputStream);
        byte[] sourceImageFileBytes = sourceImageFileByteArrayOutputStream.toByteArray();

        ImageEntity uploadedImageEntity = jiveCoreUser2.uploadImage(new FileBody(sourceImageFile, "image/jpeg")).call();

        assertThat(uploadedImageEntity, allOf(
                imageSize(greaterThan(0)),
                imageContentType("image/jpeg"),
                imageName("el-barto.jpg"),
                imageRef(notNullValue(String.class)),
                imageWidth(275),
                imageHeight(342)));

        InputStream imageInputStream = jiveCoreUser2.fetchImage(uploadedImageEntity.ref, new JiveCoreRequestOptions()).call();
        ByteArrayOutputStream imageByteArrayOutputStream = new ByteArrayOutputStream();
        ByteStreams.copy(imageInputStream, imageByteArrayOutputStream);
        imageInputStream.close();
        byte[] imageBytes = imageByteArrayOutputStream.toByteArray();
        assertArrayEquals(sourceImageFileBytes, imageBytes);

        JiveCoreRequestOptions shrunkenImageOptions = new JiveCoreRequestOptions().setWidth(27).setPreserveAspectRatio(true);
        InputStream shrunkenImageInputStream = jiveCoreUser2.fetchImage(uploadedImageEntity.ref, shrunkenImageOptions).call();
        ByteArrayOutputStream shrunkenImageByteArrayOutputStream = new ByteArrayOutputStream();
        ByteStreams.copy(shrunkenImageInputStream, shrunkenImageByteArrayOutputStream);
        shrunkenImageByteArrayOutputStream.close();
        byte[] shrunkenImageBytes = shrunkenImageByteArrayOutputStream.toByteArray();
        assertThat(imageBytes.length, greaterThan(shrunkenImageBytes.length));
    }
}
