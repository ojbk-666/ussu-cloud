package cc.ussu.modules.ecps.item.service.impl;

import cc.ussu.modules.ecps.item.entity.EbBrand;
import cc.ussu.modules.ecps.item.entity.EbCat;
import cc.ussu.modules.ecps.item.entity.EbCatbrand;
import cc.ussu.modules.ecps.item.mapper.EbBrandMapper;
import cc.ussu.modules.ecps.item.service.IEbBrandService;
import cc.ussu.modules.ecps.item.service.IEbCatService;
import cc.ussu.modules.ecps.item.service.IEbCatbrandService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 品牌 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-14
 */
@Service
public class EbBrandServiceImpl extends ServiceImpl<EbBrandMapper, EbBrand> implements IEbBrandService {

    @Autowired
    private IEbCatbrandService catbrandService;
    @Autowired
    private IEbCatService catService;

    @Override
    public EbBrand detail(Integer brandId) {
        Assert.notNull(brandId);
        EbBrand brand = new EbBrand().setBrandId(brandId).selectById();
        LambdaQueryWrapper<EbCatbrand> qw = new LambdaQueryWrapper<>();
        qw.eq(EbCatbrand::getBrandId, brandId);
        List<EbCatbrand> list = catbrandService.list(qw);
        List<Integer> catIdList = list.stream().map(EbCatbrand::getCatId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(catIdList)) {
            LambdaQueryWrapper<EbCat> qw1 = new LambdaQueryWrapper<>();
            qw1.in(EbCat::getCatId, catIdList);
            List<EbCat> catList = catService.list(qw1);
            brand.setCatList(catList);
        }
        return brand.setCatIds(catIdList);
    }

    @Transactional
    @Override
    public void add(EbBrand brand) {
        Assert.notBlank(brand.getBrandName());
        brand.insert();
        List<Integer> catIds = brand.getCatIds();
        if (CollUtil.isNotEmpty(catIds)) {
            List<EbCatbrand> catbrandList = catIds.stream()
                    .map(item -> new EbCatbrand().setBrandId(brand.getBrandId()).setCatId(item))
                    .collect(Collectors.toList());
            catbrandService.saveBatch(catbrandList, 50);
        }
    }

    @Transactional
    @Override
    public void edit(EbBrand brand) {
        Assert.notNull(brand.getBrandId());
        Assert.notBlank(brand.getBrandName());
        brand.updateById();
        LambdaQueryWrapper<EbCatbrand> qw = new LambdaQueryWrapper<EbCatbrand>()
                .eq(EbCatbrand::getBrandId, brand.getBrandId());
        catbrandService.remove(qw);
        List<Integer> catIds = brand.getCatIds();
        if (CollUtil.isNotEmpty(catIds)) {
            List<EbCatbrand> catbrandList = catIds.stream()
                    .map(item -> new EbCatbrand().setBrandId(brand.getBrandId()).setCatId(item))
                    .collect(Collectors.toList());
            catbrandService.saveBatch(catbrandList, 50);
        }
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbBrand> qw = new LambdaQueryWrapper<>();
        qw.in(EbBrand::getBrandId, ids);
        super.remove(qw);
        LambdaQueryWrapper<EbCatbrand> qw1 = new LambdaQueryWrapper<>();
        qw1.in(EbCatbrand::getBrandId, ids);
        catbrandService.remove(qw1);
    }

}
